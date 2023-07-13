package com.assignment.restapiassignment.exceptions;

public class InternalServerErrorException extends Exception{

    public InternalServerErrorException(String message){
        super(message);
    }
}
