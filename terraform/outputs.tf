# ==========================================
# 出力値
# ==========================================

output "alb_dns_name" {
  description = "ALB の DNS 名（ブラウザからアクセスする URL）"
  value       = aws_lb.main.dns_name
}

output "ecr_repository_url" {
  description = "ECR リポジトリの URL（Docker push 先）"
  value       = aws_ecr_repository.app.repository_url
}

output "aurora_endpoint" {
  description = "Aurora クラスターのエンドポイント"
  value       = module.aurora.cluster_endpoint
}

output "secrets_manager_arn" {
  description = "Secrets Manager のシークレット ARN"
  value       = aws_secretsmanager_secret.db_credentials.arn
}

output "ecs_cluster_name" {
  description = "ECS クラスター名"
  value       = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  description = "ECS サービス名"
  value       = aws_ecs_service.app.name
}

output "github_actions_role_arn" {
  description = "GitHub Actions OIDC 用 IAM ロール ARN"
  value       = aws_iam_role.github_actions.arn
}
