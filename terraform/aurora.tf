# ==========================================
# Aurora PostgreSQL 18.1 — Serverless v2
# ==========================================

# Aurora 用セキュリティグループ
resource "aws_security_group" "aurora" {
  name_prefix = "${var.project_name}-aurora-"
  vpc_id      = module.vpc.vpc_id
  description = "Aurora PostgreSQL へのアクセスを ECS タスクからのみ許可"
}

resource "aws_vpc_security_group_ingress_rule" "aurora_from_ecs" {
  security_group_id            = aws_security_group.aurora.id
  referenced_security_group_id = aws_security_group.ecs_tasks.id
  from_port                    = 5432
  to_port                      = 5432
  ip_protocol                  = "tcp"
  description                  = "ECS タスクからの PostgreSQL 接続を許可"
}

module "aurora" {
  source  = "terraform-aws-modules/rds-aurora/aws"
  version = "~> 10.0"

  name           = "${var.project_name}-${var.environment}"
  engine         = "aurora-postgresql"
  engine_version = "18.1"

  # Serverless v2（コスト最小: 未使用時はほぼ0に近い）
  serverlessv2_scaling_configuration = {
    min_capacity = 0.5  # 最小値（これ以下にはできない）
    max_capacity = 1.0  # 最大も抑える
  }

  instances = {
    one = {
      instance_class = "db.serverless"
    }
  }

  # ネットワーク
  vpc_id               = module.vpc.vpc_id
  db_subnet_group_name = module.vpc.database_subnet_group_name

  security_group_rules = {
    ecs_ingress = {
      source_security_group_id = aws_security_group.ecs_tasks.id
    }
  }

  # DB 設定
  database_name   = var.db_name
  master_username = var.db_master_username
  master_password = random_password.db_password.result

  # 暗号化
  storage_encrypted = true

  # dev 環境用の設定
  skip_final_snapshot       = true
  apply_immediately         = true
  deletion_protection       = false
  backup_retention_period   = 1

  # ログ
  enabled_cloudwatch_logs_exports = ["postgresql"]

  tags = {
    Name = "${var.project_name}-${var.environment}"
  }
}
