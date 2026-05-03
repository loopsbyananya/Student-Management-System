-- Add min_files_required to assignments
ALTER TABLE assignments ADD COLUMN min_files INT DEFAULT 1;

-- Add file_path to submissions, repurposing submission_text could be problematic if they submit both text and file.
-- Let's just use submission_text for the file path, but adding submission_date is requested.
ALTER TABLE submissions ADD COLUMN submission_date DATE;
