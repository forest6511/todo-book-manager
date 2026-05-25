# ==========================================
# メイン設定
# ==========================================
terraform {
  required_version = ">= 1.12"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.33"
    }
  }

  # S3 バックエンドを使う場合はここに設定
  # backend "s3" {
  #   bucket = "your-terraform-state-bucket"
  #   key    = "book-manager/terraform.tfstate"
  #   region = "ap-northeast-1"
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "book-manager"
      Environment = var.environment
      ManagedBy   = "terraform"
    }
  }
}

# ==========================================
# データソース
# ==========================================
data "aws_caller_identity" "current" {}
data "aws_region" "current" {}
