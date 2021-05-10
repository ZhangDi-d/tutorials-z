package org.example.independentmybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;

/**
 * @author dizhang
 * @date 2021-05-10
 */
public class TestMapper {
    static SqlSessionFactory sqlSessionFactory;

    static {
        sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
    }

    @Test
    public void testAdd() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            StudentMapper StudentMapper = sqlSession.getMapper(StudentMapper.class);
            Student Student = new Student("tom", 5);
            StudentMapper.insertStudent(Student);
            sqlSession.commit();//这里一定要提交，不然数据进不去数据库中
        }
    }

    @Test
    public void getStudent() {
         try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            StudentMapper StudentMapper = sqlSession.getMapper(StudentMapper.class);
            Student Student = StudentMapper.getStudent(1);
            System.out.println("name: " + Student.getName() + "|age: " + Student.getAge());
        }
    }
}