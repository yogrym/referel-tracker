package com.refply.mvp.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.refply.mvp.entity.ReferelProgramEntity;
import com.refply.mvp.service.GeneralUserService;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @Autowired
    private GeneralUserService genService;

    @GetMapping("/ref/{uniq-code}")
    public ResponseEntity<?> getPublicProgram(@PathVariable("uniq-code") String uniqCode) {
        ReferelProgramEntity program = genService.getPublicProgramByCode(uniqCode);

        if (program != null) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "program", program
            ));
        }

        return ResponseEntity.status(404).body(Map.of("message", "Program not found"));
    }
}
