package com.example.Triple_clone.service.reservation;

import org.python.core.*;
import org.python.util.PythonInterpreter;

public class PythonService {
    public void findAccommodation(String local) throws PyException {
        PythonInterpreter Python_Interpreter = new PythonInterpreter();
        System.out.println("Hello, This is Delftstack.com! The Best Tutorial Site!, Message from Java");

        // Run a Python file
        Python_Interpreter.execfile("C:\\Users\\USER\\Desktop\\공부\\멘토링\\Triple_clone\\src\\main\\resources\\웹 크롤링 파이썬.py");
    }
}
