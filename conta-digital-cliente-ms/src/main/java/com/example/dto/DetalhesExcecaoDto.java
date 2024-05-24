package com.example.dto;

import java.time.LocalDateTime;

public record DetalhesExcecaoDto(LocalDateTime timestamp, Integer status, String error, String path) {

}
