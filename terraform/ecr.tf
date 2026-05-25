# ==========================================
# ECR — Docker イメージリポジトリ
# ==========================================
resource "aws_ecr_repository" "app" {
  name                 = "${var.project_name}-${var.environment}"
  image_tag_mutability = "MUTABLE"
  force_delete         = true # dev 環境での削除を容易にする

  image_scanning_configuration {
    scan_on_push = true
  }
}

# 古いイメージの自動削除ポリシー（最新10件のみ保持）
resource "aws_ecr_lifecycle_policy" "app" {
  repository = aws_ecr_repository.app.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "最新10件のイメージのみ保持"
        selection = {
          tagStatus   = "any"
          countType   = "imageCountMoreThan"
          countNumber = 10
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}
