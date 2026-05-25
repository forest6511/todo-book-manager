# ==========================================
# VPC — パブリック/プライベートサブネット構成
# ==========================================
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"

  name = "${var.project_name}-${var.environment}"
  cidr = var.vpc_cidr
  azs  = var.availability_zones

  # パブリックサブネット（ALB 用）
  public_subnets = [
    cidrsubnet(var.vpc_cidr, 8, 1),
    cidrsubnet(var.vpc_cidr, 8, 2),
  ]

  # プライベートサブネット（ECS タスク・Aurora 用）
  private_subnets = [
    cidrsubnet(var.vpc_cidr, 8, 11),
    cidrsubnet(var.vpc_cidr, 8, 12),
  ]

  # データベース用サブネット
  database_subnets = [
    cidrsubnet(var.vpc_cidr, 8, 21),
    cidrsubnet(var.vpc_cidr, 8, 22),
  ]

  # NAT Gateway（ECS タスクが ECR からイメージを取得するため）
  # 月額約$45。VPC エンドポイントに切り替えるとさらに安くなる
  enable_nat_gateway = true
  single_nat_gateway = true # コスト最小: AZ 冗長なし

  # DNS
  enable_dns_hostnames = true
  enable_dns_support   = true

  # データベースサブネットグループを自動作成
  create_database_subnet_group = true

  tags = {
    Name = "${var.project_name}-${var.environment}"
  }
}
