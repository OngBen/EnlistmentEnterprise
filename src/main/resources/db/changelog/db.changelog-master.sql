-- liquibase formatted sql

-- changeset root:1679306066401-1
CREATE TABLE "public"."admin" ("id" INTEGER NOT NULL, "firstname" TEXT, "lastname" TEXT, CONSTRAINT "admin_pkey" PRIMARY KEY ("id"));

-- changeset root:1679306066401-2
CREATE TABLE "public"."room" ("name" TEXT NOT NULL, "capacity" INTEGER NOT NULL, "version" INTEGER DEFAULT 0 NOT NULL, CONSTRAINT "room_pkey" PRIMARY KEY ("name"));

-- changeset root:1679306066401-3
CREATE TABLE "public"."room_sections" ("room_name" TEXT NOT NULL, "sections_section_id" TEXT NOT NULL);

-- changeset root:1679306066401-4
CREATE TABLE "public"."section" ("section_id" TEXT NOT NULL, "number_of_students" INTEGER NOT NULL, "days" INTEGER, "end_time" time(6) WITHOUT TIME ZONE, "start_time" time(6) WITHOUT TIME ZONE, "version" INTEGER DEFAULT 0 NOT NULL, "room_name" TEXT, "subject_subject_id" TEXT, CONSTRAINT "section_pkey" PRIMARY KEY ("section_id"));

-- changeset root:1679306066401-5
CREATE TABLE "public"."student" ("student_number" INTEGER NOT NULL, "firstname" TEXT, "lastname" TEXT, CONSTRAINT "student_pkey" PRIMARY KEY ("student_number"));

-- changeset root:1679306066401-6
CREATE TABLE "public"."student_sections" ("student_student_number" INTEGER NOT NULL, "sections_section_id" TEXT NOT NULL);

-- changeset root:1679306066401-7
CREATE TABLE "public"."student_subjects_taken" ("student_student_number" INTEGER NOT NULL, "subjects_taken_subject_id" TEXT NOT NULL);

-- changeset root:1679306066401-8
CREATE TABLE "public"."subject" ("subject_id" TEXT NOT NULL, CONSTRAINT "subject_pkey" PRIMARY KEY ("subject_id"));

-- changeset root:1679306066401-9
CREATE TABLE "public"."subject_prerequisites" ("subject_subject_id" TEXT NOT NULL, "prerequisites_subject_id" TEXT NOT NULL);

-- changeset root:1679306066401-10
ALTER TABLE "public"."room_sections" ADD CONSTRAINT "uk_4y9hvexlvcosyoi6sxolp9if2" UNIQUE ("sections_section_id");

-- changeset root:1679306066401-11
ALTER TABLE "public"."room_sections" ADD CONSTRAINT "fk18tmp1kdjwlixo8tcyag4xnv" FOREIGN KEY ("sections_section_id") REFERENCES "public"."section" ("section_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-12
ALTER TABLE "public"."subject_prerequisites" ADD CONSTRAINT "fk6ta30r0qhlti2fsjg762emtwb" FOREIGN KEY ("prerequisites_subject_id") REFERENCES "public"."subject" ("subject_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-13
ALTER TABLE "public"."student_sections" ADD CONSTRAINT "fk89rtc8dmim7fd4lulgkecplhj" FOREIGN KEY ("sections_section_id") REFERENCES "public"."section" ("section_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-14
ALTER TABLE "public"."section" ADD CONSTRAINT "fkb7vxfqjj8qa0r38kh4qs1eac1" FOREIGN KEY ("room_name") REFERENCES "public"."room" ("name") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-15
ALTER TABLE "public"."student_sections" ADD CONSTRAINT "fkcbuj7ic505urpqreop2xuwstg" FOREIGN KEY ("student_student_number") REFERENCES "public"."student" ("student_number") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-16
ALTER TABLE "public"."room_sections" ADD CONSTRAINT "fkfd4bv684oyhmv5ntq0gll0exl" FOREIGN KEY ("room_name") REFERENCES "public"."room" ("name") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-17
ALTER TABLE "public"."student_subjects_taken" ADD CONSTRAINT "fkitrqnu6k56yf07ntuo8q1rast" FOREIGN KEY ("subjects_taken_subject_id") REFERENCES "public"."subject" ("subject_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-18
ALTER TABLE "public"."section" ADD CONSTRAINT "fkn23lvqkfvxq9wo0w6qt9xgomd" FOREIGN KEY ("subject_subject_id") REFERENCES "public"."subject" ("subject_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-19
ALTER TABLE "public"."subject_prerequisites" ADD CONSTRAINT "fkogx9mcr8od8y3uf7cxb0spqed" FOREIGN KEY ("subject_subject_id") REFERENCES "public"."subject" ("subject_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset root:1679306066401-20
--validCheckSum: any
ALTER TABLE "public"."student_subjects_taken" ADD CONSTRAINT "fkr7ky8jeghxfcdqfugflneppgv" FOREIGN KEY ("student_student_number") REFERENCES "public"."student" ("student_number") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- liquibase formatted sql

-- liquibase formatted sql

-- liquibase formatted sql

-- liquibase formatted sql

-- changeset Ong_Family's:1679998351748-1
CREATE TABLE public.faculty (faculty_number INTEGER NOT NULL, first_name TEXT, last_name TEXT, version INTEGER DEFAULT 0 NOT NULL, CONSTRAINT "facultyPK" PRIMARY KEY (faculty_number));

-- changeset Ong_Family's:1679998351748-2
CREATE TABLE public.faculty_sections (faculty_faculty_number INTEGER NOT NULL, sections_section_id TEXT NOT NULL);

-- changeset Ong_Family's:1679998351748-3
ALTER TABLE public.section ADD instructor_faculty_number INTEGER;

-- changeset Ong_Family's:1679998351748-4
ALTER TABLE public.faculty_sections ADD CONSTRAINT "UK_2ce4o3g0a4rl929cbewtmmu1y" UNIQUE (sections_section_id);

-- changeset Ong_Family's:1679998351748-5
ALTER TABLE public.faculty_sections ADD CONSTRAINT "FK2j11yqgbi4wdcyov12kgr5xy0" FOREIGN KEY (sections_section_id) REFERENCES public.section (section_id);

-- changeset Ong_Family's:1679998351748-6
ALTER TABLE public.faculty_sections ADD CONSTRAINT "FK656uje4ggsa8h9sqf9yfji4n" FOREIGN KEY (faculty_faculty_number) REFERENCES public.faculty (faculty_number);

-- changeset Ong_Family's:1679998351748-7
ALTER TABLE public.section ADD CONSTRAINT "FKhbrcxeiyot9gwvpjd9klgc0rw" FOREIGN KEY (instructor_faculty_number) REFERENCES public.faculty (faculty_number);

