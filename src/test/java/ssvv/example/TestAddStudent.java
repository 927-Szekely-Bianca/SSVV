package ssvv.example;

import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddStudent {
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        File xml = new File("fisiere/studentiTest.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xml))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<inbox>\n" +
                    "\n" +
                    "</inbox>");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setup() {
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.studentValidator = new StudentValidator();
        this.service = new Service(this.studentFileRepository, this.studentValidator, null, null, null, null);
    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/studentiTest.xml").delete();
    }

    @Test
    public void testAddStudentDuplicate(){
        Student newStudent1 = new Student("1", "Ale", 931, "ale@gmail.com");

        Student stud1 = this.service.addStudent(newStudent1);
        assertNull(stud1);

        Student stud2 = this.service.addStudent(newStudent1);
        assertEquals(newStudent1.getID(), stud2.getID());

        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentNonDuplicate(){
        Student newStudent1 = new Student("1", "Ale", 931, "ale@gmail.com");
        Student newStudent2 = new Student("2", "Ale", 931, "ale@gmail.com");


        Student stud1 = this.service.addStudent(newStudent1);
        assertNull(stud1);

        Student stud2 = this.service.addStudent(newStudent2);
        assertNull(stud2);

        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        assertEquals(students.next().getID(), newStudent2.getID());

        this.service.deleteStudent("1");
        this.service.deleteStudent("2");
    }

    @Test
    public void testAddStudentValidName(){
        Student newStudent1 = new Student("1", "Ale", 931, "ale@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentEmptyName(){
        Student newStudent2 = new Student("2", "", 931, "ale@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));

    }

    @Test
    public void testAddStudentNullName(){
        Student newStudent3 = new Student("3", null, 931, "ale@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }



    @Test
    public void testAddStudentValidGroup() {
        Student newStudent1 = new Student("1", "Ale", 931, "ale@gmail.com");

        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());

        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentInvalidGroup() {
        Student newStudent2 = new Student("2", "Ale", -6, "ale@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentValidEmail() {
        Student newStudent1 = new Student("1", "Ale", 931, "ale@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentEmptyEmail() {
        Student newStudent2 = new Student("2", "Ale", 931, "");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentNullEmail() {
        Student newStudent3 = new Student("3", "Ale", 931, null);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    public void testAddStudentValidId() {
        Student newStudent1 = new Student("2345", "Ale", 931, "ale@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("2345");
    }

    @Test
    public void testAddStudentEmptyId() {
        Student newStudent2 = new Student("", "Ale", 931, "ale@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentNullId() {
        Student newStudent3 = new Student(null, "Ale", 931, "ale@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    /**
     * BVA Test case
     */
    @Test
    public void testAddStudentGroupLowerBVABound(){
        Student newStudent1 = new Student("1", "Ale", 0, "ale@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

}