package com.example.storesaas.identity.user;

import com.example.storesaas.identity.user.dto.StaffCreateDTO;
import com.example.storesaas.identity.user.vo.StaffVO;
import com.example.storesaas.identity.user.dto.StaffUpdateDTO;

import java.util.List;

public interface StaffService {
    List<StaffVO> list();

    StaffVO create(StaffCreateDTO request);

    StaffVO update(Long id, StaffUpdateDTO request);

    StaffVO setStatus(Long id, Integer status);
}
