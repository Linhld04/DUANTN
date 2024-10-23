package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class SanPhamService {
    @Autowired
    private static SanPhamRepository sanPhamRepository;


    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }


    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    public List<Object[]> getAllSanPhamAD() {
        return sanPhamRepository.getAllSanPhamAD();
    }
    public List<Object[]> getSanPhamById(String idSanPham) {
        return sanPhamRepository.getSanPhamById(idSanPham);
    }


    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public SanPham updateSanPham(Integer idSanPham, SanPham sanPham) {
        sanPham.setIdSanPham(idSanPham);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer idSanPham) {
        // Xóa các hình ảnh liên quan
        sanPhamRepository.deleteHinhAnhBySanPhamId(idSanPham);

        // Xóa các chi tiết sản phẩm liên quan
        sanPhamRepository.deleteChiTietBySanPhamId(idSanPham);

        // Xóa sản phẩm chính
        sanPhamRepository.deleteSanPhamByIdSanPham(idSanPham);
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    public SanPham getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    public SanPham createSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public SanPham updateSanPhams(Integer id, SanPham sanPham) {
        sanPham.setIdSanPham(id);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPhams(Integer id) {
        sanPhamRepository.deleteById(id);
    }


    @Transactional
    public Integer addSanPham(Integer idDanhMuc, String tenSanPham, BigDecimal giaBan, String moTa, Date ngayTao, Date ngayCapNhat, Boolean trangThai) {
        Integer idSanPham = sanPhamRepository.addSanPham(idDanhMuc, tenSanPham, giaBan, moTa, ngayTao, ngayCapNhat, trangThai);
        return idSanPham;
    }


    public void addHinhAnhSanPham(Integer idSanPham, String urlAnh, Integer thuTu, String loaiHinhAnh) {
        // Log thông tin ID sản phẩm
        System.out.println("Adding image for product ID: " + idSanPham);
        sanPhamRepository.addHinhAnhSanPham(idSanPham, urlAnh, thuTu, loaiHinhAnh);
    }
    public Integer getLatestSanPhamId() {
        // Lấy danh sách ID sản phẩm mới nhất, chỉ lấy 1 phần tử đầu tiên
        List<Integer> result = sanPhamRepository.getLatestSanPhamId();
        if (result != null && !result.isEmpty()) {
            return result.get(0);  // Trả về ID sản phẩm mới nhất
        } else {
            return null;  // Trường hợp không có sản phẩm
        }
    }


    @Transactional
    public void toggleStatusById(Integer idSanPham) {
        sanPhamRepository.updateStatusById(idSanPham);
    }


}
