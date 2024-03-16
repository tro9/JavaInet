/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan8;

/**
 *
 * @author Admin
 */
import java.io.*;

// Lớp cần tuần tự hóa
//class Person implements Serializable {
//    private static final long serialVersionUID = 1L;
//    private String name;
//    private int age;
//
//    // No-argument constructor
//    public Person() {
//        // Default constructor
//    }
//
//    // Parameterized constructor
//    public Person(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    // Getters, setters, and other methods...
//}


public class SerializationExample {
    public static void main(String[] args) {
        // Tạo một đối tượng Person
        Person person = new Person("John", 30);

        // Ghi đối tượng vào file
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
            outputStream.writeObject(person);
            System.out.println("Object serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Đọc đối tượng từ file
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("person.ser"))) {
            Person serializedPerson = (Person) inputStream.readObject();
            System.out.println("Object deserialized successfully.");
            System.out.println("Deserialized Person: " + serializedPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

