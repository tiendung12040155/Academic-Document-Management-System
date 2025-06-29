package com.example.ADMS.controller;


import com.example.ADMS.entity.type.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ADMS.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/helper")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Helper", description = "API for Helper")
@CrossOrigin
public class HelperController {

    @GetMapping("/list-visual-type")
    public ResponseEntity<?> getListVisualType() {
        return ResponseEntity.ok(VisualType.values());
    }

    @GetMapping("/list-action-type")
    public ResponseEntity<?> getListActionType() {
        return ResponseEntity.ok(ActionType.values());
    }

    @GetMapping("/list-approve-type")
    public ResponseEntity<?> getListApproveType() {
        return ResponseEntity.ok(ApproveType.values());
    }

    @GetMapping("/list-method-type")
    public ResponseEntity<?> getListMethodType() {
        return ResponseEntity.ok(MethodType.values());
    }

    @GetMapping("/list-permission-resource-type")
    public ResponseEntity<?> getListPermissionResourceType() {
        return ResponseEntity.ok(PermissionResourceType.values());
    }

    @GetMapping("/list-resource-type")
    public ResponseEntity<?> getListResourceType() {
        return ResponseEntity.ok(ResourceType.values());
    }

    @GetMapping("/list-table-type")
    public ResponseEntity<?> getListTableType() {
        return ResponseEntity.ok(TableType.values());
    }

    @GetMapping("/list-tab-resource-type")
    public ResponseEntity<?> getListTabResourceType() {
        return ResponseEntity.ok(TabResourceType.values());
    }

}
