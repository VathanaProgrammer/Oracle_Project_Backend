select * from tbl_subjects
select * from tbl_majors
select * from tbl_question
select * from tbl_exam_files
select * from tbl_question
select * from tbl_question_options
select * from tbl_exams
select * from tbl_exam_questions
select * from tbl_departments
SELECT * FROM tbl_assign_to
select * from tbl_users
select * from TBL_TEACHING_ASSIGNMENTS
select * from tbl_majors
INSERT INTO tbl_subjects values(1, 'C#')
COMMIT;
INSERT INTO tbl_assign_to VALUES (1, 8, 'Kok Slaket','Evening', '05:30 - 08:30 PM', 2, 1, 1);
INSERT INTO tbl_assign_to VALUES (2, 8, 'Kok Slaket','Evening', '02:15 - 05:15 PM', 2, 1, 1);
INSERT INTO tbl_teaching_assignments VALUES (1,1,1);
INSERT INTO tbl_teaching_assignments VALUES (2,2,1);
INSERT INTo tbl_users VALUES (1, 'fake123@gmail.com', 'fake_first_name', 'fake_last_name', 'fake123', '09822232','fale/fale_location', 'TEACHER', 'fake_teacher')
INSERT INTO tbl_departments VALUES (1, 'Science and Technology')
INSERT INTO tbl_majors VALUES (1, 'Computer Science', 1)
BEGIN
  FOR t IN (SELECT table_name FROM user_tables) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE '  t.table_name  ' CASCADE CONSTRAINTS';
  END LOOP;
END;
COMMIT 

SELECT cols.table_name, cols.column_name, cons.constraint_name, cons.constraint_type
FROM all_constraints cons
JOIN all_cons_columns cols ON cons.constraint_name = cols.constraint_name
WHERE cons.constraint_type = 'U'  -- U = Unique constraint
AND cons.constraint_name = 'UKHUM1IH794WE213MRAEVYA388U';
ALTER TABLE tbl_exams DROP CONSTRAINT UKHUM1IH794WE213MRAEVYA388U;

ALTER USER finalpro QUOTA UNLIMITED ON USERS;
