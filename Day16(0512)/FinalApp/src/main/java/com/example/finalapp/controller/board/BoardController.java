package com.example.finalapp.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

//    경로가 같으면 void해도 자동 매핑됨
    @GetMapping("/list")
    public void boardList() {}

    @GetMapping("/details")
    public void boardDetail() {}

    @GetMapping("/write")
    public void boardWrite() {}
}
