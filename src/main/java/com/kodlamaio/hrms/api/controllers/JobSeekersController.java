package com.kodlamaio.hrms.api.controllers;

import com.kodlamaio.hrms.business.abstracts.accounts.JobSeekerService;
import com.kodlamaio.hrms.core.utilities.results.ErrorDataResult;
import com.kodlamaio.hrms.core.utilities.results.Result;
import com.kodlamaio.hrms.entities.concretes.users.JobSeeker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jobseeker/")
public class JobSeekersController {

    private JobSeekerService jobSeekerService;

    public JobSeekersController(JobSeekerService jobSeekerService) {

        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping(name = "getall")
    public Result getAll(){
        return this.jobSeekerService.getAll();
    }

    @PostMapping(name = "registery")
    public ResponseEntity<?> registery(@RequestBody JobSeeker jobSeeker){
        return ResponseEntity.ok(this.jobSeekerService.add(jobSeeker));
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    @ResponseBody
    public Result uploadImage(@RequestParam int id, @RequestPart("file") MultipartFile image) {
        return this.jobSeekerService.addImage(id, image);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException
            (MethodArgumentNotValidException exceptions){
        Map<String,String> validationErrors = new HashMap<String, String>();
        for(FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorDataResult<Object> errors
                = new ErrorDataResult<Object>(validationErrors,"Doğrulama hataları");
        return errors;
    }


}
