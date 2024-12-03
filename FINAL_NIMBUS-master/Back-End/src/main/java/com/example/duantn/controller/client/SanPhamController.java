package com.example.duantn.controller.client;

import com.example.duantn.entity.SanPham;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;
    @GetMapping("/all")
    public List<SanPham> getSanPhamForBanHang() {
        return sanPhamService.getSanPhamForBanHang();
    }
    @GetMapping("/all-quay")
    public List<Map<String, Object>> getSPBanHangTaiQuay() {
        List<Map<String, Object>> results = sanPhamService.getSPBanHangTaiQuay();
        return results;
    }

    @GetMapping("/chitiet")
    public List<Map<String, Object>> getSanPhamChiTiet(@RequestParam("id_san_pham") Integer idSanPham) {
        // Gọi service để lấy dữ liệu từ database
        List<Object[]> rawResults = sanPhamService.getSanPhamChiTiet(idSanPham);

        // Chuyển đổi Object[] thành Map với tên trường rõ ràng
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] result : rawResults) {
            Map<String, Object> data = new HashMap<>();
            data.put("id_san_pham_chi_tiet", result[0]);
            data.put("id_san_pham", result[1]);
            data.put("ma_san_pham", result[2]);
            data.put("ten_san_pham", result[3]);
            data.put("so_luong", result[4]);
            data.put("ten_chat_lieu", result[5]);
            data.put("ten_mau_sac", result[6]);
            data.put("ten_kich_thuoc", result[7]);
            data.put("mo_ta", result[8]);
            data.put("ten_dot_giam_gia", result[9]);
            data.put("gia_khuyen_mai", result[10]);
            data.put("gia_tri_giam_gia", result[11]);
            data.put("kieu_giam_gia", result[12]);
            data.put("ngay_bat_dau", result[13]);
            data.put("ngay_ket_thuc", result[14]);
            data.put("gia_ban", result[15]);
            response.add(data);
        }

        return response;
    }

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("trangThai", row[2]);
        map.put("giaBan", row[3]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[4]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[5]); // Cập nhật chỉ số cho trạng thái
        map.put("soLuong", row[6]);   // Cập nhật chỉ số cho URL ảnh
        map.put("urlAnh", row[7]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[8]);       // Danh sách kích thước
        return map;
    }
    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }
    private Map<String, Object> mapDanhMucDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("trangThai", row[2]);
        map.put("giaBan", row[3]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[4]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[5]); // Cập nhật chỉ số cho trạng thái
        map.put("urlAnh", row[6]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[7]);       // Danh sách kích thước
        return map;
    }
    private List<Map<String, Object>> mapDanhMucs(List<Object[]> results) {
        return results.stream().map(this::mapDanhMucDetail).collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhams();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }


    @GetMapping("/findSanPham/{idSanPham}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamById(@PathVariable String idSanPham) {
        List<Object[]> sanPhamDetails = sanPhamService.getSanPhamById(idSanPham);
        if (sanPhamDetails.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Trả về tất cả các sản phẩm chi tiết
        List<Map<String, Object>> productDetails = mapSanPhams(sanPhamDetails);
        return ResponseEntity.ok(productDetails);
    }


    @GetMapping("/findDanhMuc/{idDanhMuc}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDanhMuc(@PathVariable Integer idDanhMuc) {
        return ResponseEntity.ok(mapDanhMucs(sanPhamService.getSanPhamsByDanhMuc(idDanhMuc)));
    }


}
