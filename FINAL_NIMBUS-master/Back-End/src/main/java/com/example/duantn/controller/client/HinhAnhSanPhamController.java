package com.example.duantn.controller.client;

import com.example.duantn.entity.HinhAnhSanPham;
import com.example.duantn.service.HinhAnhSanPhamService;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hinh_anh")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HinhAnhSanPhamController {

    @Autowired
    private HinhAnhSanPhamService hinhAnhSanPhamService;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/{idSanPham}")
    public ResponseEntity<List<HinhAnhSanPham>> getHinhAnhBySanPhamId(@PathVariable Integer idSanPham) {
        List<HinhAnhSanPham> hinhAnhs = hinhAnhSanPhamService.getHinhAnhBySanPhamId(idSanPham);
        if (hinhAnhs != null && !hinhAnhs.isEmpty()) {
            return ResponseEntity.ok(hinhAnhs);
        }
        return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy hình ảnh
    }
}
