# ==========================================
# ステージ 1: Astro フロントエンドのビルド
# ==========================================
FROM node:22-slim AS frontend-builder
WORKDIR /frontend
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# ==========================================
# ステージ 2: Spring Boot アプリケーションのビルド
# ==========================================
FROM eclipse-temurin:21-jdk AS backend-builder
WORKDIR /app
COPY backend/gradlew ./
COPY backend/gradle/ gradle/
COPY backend/build.gradle.kts backend/settings.gradle.kts ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon
COPY backend/src/ src/

# Astro のビルド成果物を static/ に配置
COPY --from=frontend-builder /frontend/dist/ src/main/resources/static/

RUN ./gradlew bootJar --no-daemon

# ==========================================
# ステージ 3: レイヤー分割（Docker キャッシュ最適化）
# ==========================================
FROM eclipse-temurin:21-jdk AS extractor
WORKDIR /builder
COPY --from=backend-builder /app/build/libs/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# ==========================================
# ステージ 4: 本番ランタイム（JRE のみ）
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /application

# 各レイヤーを個別コピー（依存が変わらなければキャッシュヒット）
COPY --from=extractor /builder/extracted/dependencies/ ./
COPY --from=extractor /builder/extracted/spring-boot-loader/ ./
COPY --from=extractor /builder/extracted/snapshot-dependencies/ ./
COPY --from=extractor /builder/extracted/application/ ./

# セキュリティ: root ユーザーで実行しない
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
