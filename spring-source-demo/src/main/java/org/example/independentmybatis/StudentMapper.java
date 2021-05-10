package org.example.independentmybatis;

/**
 * @author dizhang
 * @date 2021-05-10
 */
public interface StudentMapper {
    public void insertStudent(Student student);

    public Student getStudent(Integer id);
}
