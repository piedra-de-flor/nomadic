package com.example.Triple_clone.service.reservation;

import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScrapingService {
    public List<String> findAccommodations(String local, String sort, long page) throws PyException {
        PythonInterpreter Python_Interpreter = new PythonInterpreter();

        Python_Interpreter.execfile("C:\\Users\\USER\\Desktop\\공부\\멘토링\\Triple_clone\\src\\main\\resources\\웹 크롤링 파이썬.py");
    }
}
