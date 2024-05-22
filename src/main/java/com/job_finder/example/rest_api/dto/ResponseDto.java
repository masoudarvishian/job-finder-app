package com.job_finder.example.rest_api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto<K> {
    K data;
}
