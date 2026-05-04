package com.equabli.collectprism.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equabli.common.response.CommonResponse;
import com.equabli.collectprism.approach.validationhandler.ValidationResult;
import com.equabli.datascrubbing.service.PlacementService;

@RestController
@RequestMapping("/account")
public class PlacementController {
	
	@Autowired
	PlacementService placementService;
	  
	@PostMapping("/placement")
    public CommonResponse<List<ValidationResult>> handlePlacement() {
		return placementService.handlePlacement();
      
    }
	
	 @GetMapping("/api/v1/entity")
	    public CommonResponse<String> updateEntity(@RequestParam String entityname) {
		 return placementService.updateEntity(entityname);
	      
	    }
}

