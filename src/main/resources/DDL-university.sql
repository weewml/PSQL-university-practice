-- Сначала удалить старую базу данных, если она была создана
DROP TABLE IF EXISTS students, professors, courses, enrollments;

CREATE TABLE students (
	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    full_name varchar(300),
    group_name varchar(20),
    education_level varchar(100),		

    --Проверки, что имя, группа и уровень образования не пустые
    CONSTRAINT students_check_full_name_not_empty CHECK (full_name IS NOT NULL AND trim(full_name) <> ''),
    CONSTRAINT students_check_group_name_not_empty CHECK (group_name IS NOT NULL AND trim(group_name) <> ''),
    CONSTRAINT students_check_education_level_not_empty CHECK (education_level IS NOT NULL AND trim(education_level) <> '')
);

CREATE TABLE professors (
	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	full_name varchar(300),
	department varchar(300),
	
	--Проверка, что имя и кафедра не пустые
	CONSTRAINT professors_check_full_name_not_empty CHECK (full_name IS NOT NULL AND (trim(full_name)) <> ''),
    CONSTRAINT professors_check_department_not_empty CHECK (department IS NOT NULL AND (trim(department)) <> '')
);

CREATE TABLE courses (
	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,       
    title varchar(200),
    id_professor int not NULL,
    
    --Проверка, что название не пустое
    CONSTRAINT courses_check_title_not_empty CHECK (title IS NOT NULL AND trim(title) <> ''),
    
    --Внешний ключ, не дает удалить профессора, пока у него есть курсы
    CONSTRAINT courses_professors_fk FOREIGN KEY (id_professor) REFERENCES professors(id) ON DELETE RESTRICT
);

CREATE TABLE enrollments (    
	id_student int NOT NULL,
	id_course int NOT NULL,
    grade varchar(20),
    start_date date NOT NULL DEFAULT CURRENT_DATE,

    --Составной идентификатор (первичный ключ)
    CONSTRAINT enrollments_pk PRIMARY KEY (id_student, id_course),  

    --Проверка, что оценка не пустая
    CONSTRAINT enrollments_check_grade_not_empty CHECK (grade IS NOT NULL AND trim(grade) <> ''),
    
    --Внешний ключ не дает удалить студента и курс, пока у студента есть запись на курс
    CONSTRAINT enrollments_students_fk FOREIGN KEY (id_student) REFERENCES students(id) ON DELETE RESTRICT,
    CONSTRAINT enrollments_course_fk FOREIGN KEY (id_course) REFERENCES courses(id) ON DELETE RESTRICT
);