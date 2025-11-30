CREATE TABLE monthly_report (
    id BIGSERIAL PRIMARY KEY,
    month VARCHAR(20) NOT NULL,
    year INT NOT NULL,
    total_posts BIGINT DEFAULT 0,
    total_comments BIGINT DEFAULT 0
);
