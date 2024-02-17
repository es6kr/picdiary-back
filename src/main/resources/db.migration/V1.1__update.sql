-- Add 'updated_at' column to the user table
ALTER TABLE user
ADD COLUMN `updated_at` DATETIME;

-- Add columns to the diary table
ALTER TABLE diary
ADD COLUMN `date` DATE,
ADD COLUMN `image_file_name` VARCHAR(255),
ADD COLUMN `updated_at` DATETIME;

-- Create ToDo table
CREATE TABLE IF NOT EXISTS ToDo (
  id INT PRIMARY KEY AUTO_INCREMENT,
  content VARCHAR(255) NOT NULL,
  date DATETIME NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  diary_id BIGINT NOT NULL,
  FOREIGN KEY (diary_id) REFERENCES diary(id)
);
