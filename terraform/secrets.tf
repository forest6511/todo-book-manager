# ==========================================
# Secrets Manager — DB 認証情報
# ECS タスク定義から環境変数として注入される
# ==========================================

# DB パスワードをランダム生成
resource "random_password" "db_password" {
  length  = 32
  special = false # Aurora の制約に合わせて特殊文字なし
}

# シークレット本体
resource "aws_secretsmanager_secret" "db_credentials" {
  name                    = "${var.project_name}/${var.environment}/db-credentials"
  description             = "書籍管理アプリの Aurora PostgreSQL 認証情報"
  recovery_window_in_days = 0 # dev 環境では即時削除可能にする
}

# シークレットの値（JSON 形式で格納）
resource "aws_secretsmanager_secret_version" "db_credentials" {
  secret_id = aws_secretsmanager_secret.db_credentials.id
  secret_string = jsonencode({
    username = var.db_master_username
    password = random_password.db_password.result
    host     = module.aurora.cluster_endpoint
    port     = 5432
    dbname   = var.db_name
  })
}
