-- 書籍管理テーブル
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    memo VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'UNREAD',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 初期データ
INSERT INTO books (title, author, isbn, memo, status) VALUES
    ('リーダブルコード', 'Dustin Boswell', '978-4873115658', 'コードの読みやすさに関する名著', 'COMPLETED'),
    ('Clean Architecture', 'Robert C. Martin', '978-4048930659', 'ソフトウェアアーキテクチャの原則', 'READING'),
    ('ドメイン駆動設計入門', '成瀬允宣', '978-4798150727', 'DDD の実践的な入門書', 'UNREAD');
