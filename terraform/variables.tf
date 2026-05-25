# ==========================================
# 変数定義
# ==========================================

variable "aws_region" {
  description = "AWS リージョン"
  type        = string
  default     = "ap-northeast-1"
}

variable "environment" {
  description = "環境名 (dev / staging / prod)"
  type        = string
  default     = "dev"
}

variable "project_name" {
  description = "プロジェクト名（各リソースのプレフィックスに使用）"
  type        = string
  default     = "book-manager"
}

# VPC
variable "vpc_cidr" {
  description = "VPC の CIDR ブロック"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "使用するアベイラビリティゾーン"
  type        = list(string)
  default     = ["ap-northeast-1a", "ap-northeast-1c"]
}

# Aurora
variable "db_name" {
  description = "データベース名"
  type        = string
  default     = "bookmanager"
}

variable "db_master_username" {
  description = "DB マスターユーザー名"
  type        = string
  default     = "bookadmin"
}

# ECS
variable "app_port" {
  description = "アプリケーションポート"
  type        = number
  default     = 8080
}

variable "ecs_cpu" {
  description = "ECS タスクの CPU (256 = 0.25 vCPU, 最小)"
  type        = number
  default     = 256
}

variable "ecs_memory" {
  description = "ECS タスクのメモリ (MiB)"
  type        = number
  default     = 512
}

variable "ecs_desired_count" {
  description = "ECS タスクの起動数"
  type        = number
  default     = 1
}

# CI/CD
variable "github_repository" {
  description = "GitHub リポジトリ名 (owner/repo 形式)"
  type        = string
  default     = "forest6511/todo-book-manager"
}
