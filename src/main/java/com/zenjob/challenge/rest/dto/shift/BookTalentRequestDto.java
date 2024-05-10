package com.zenjob.challenge.rest.dto.shift;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class BookTalentRequestDto {
    private UUID talentId;
}
