package com.example.duantn.service;

import com.example.duantn.dto.ProductDetailUpdateRequest;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private ChatLieuChiTietRepository chatLieuChiTietRepository; // Change to correct type
    @Autowired
    private MauSacChiTietRepository mauSacChiTietRepository; // Change to correct type
    @Autowired
    private KichThuocChiTietRepository kichThuocChiTietRepository; // Change to correct type

    public List<SanPhamChiTiet> getAll() {
        return sanPhamChiTietRepository.findAll();
    }

    public List<Object[]> getById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getSanPhamById(idSanPhamCT);
    }
    public List<Object[]> getSanPhamCTById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getSanPhamCTByIdSanPham(idSanPhamCT);
    }
    public List<Object[]> getMauSacById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getMauSacByIdSanPham(idSanPhamCT);
    }

    public List<Object[]> getKichThuocById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getKichThuocByIdSanPham(idSanPhamCT);
    }

    public List<Object[]> getChatLieuById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getChatLieuByIdSanPham(idSanPhamCT);
    }

    public SanPhamChiTiet create(SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public SanPhamChiTiet update(Integer id, SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setIdSanPhamChiTiet(id);
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public List<SanPhamChiTietDTO> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAllSanPhamChiTietDetails();
    }

    public void deleteById(Integer idSanPhamCT) {
        sanPhamChiTietRepository.deleteById(idSanPhamCT);
    }

    // Giả sử bạn có một phương thức lưu cho ChatLieuChiTiet

    public void updateSoLuongSanPhamCT(List<ProductDetailUpdateRequest> payload) {
        for (ProductDetailUpdateRequest request : payload) {
            System.out.println("Updating ID: " + request.getIdSanPhamCT() + " with Quantity: " + request.getNewQuantity());

            // Kiểm tra xem giá trị có hợp lệ không
            if (request.getNewQuantity() == null) {
                System.out.println("New quantity is NULL for ID: " + request.getIdSanPhamCT());
            }

            sanPhamChiTietRepository.updateSoLuongSanPhamCT(request.getNewQuantity(), request.getIdSanPhamCT());

            // Kiểm tra lại giá trị đã cập nhật
            Integer updatedQuantity = sanPhamChiTietRepository.findQuantityById(request.getIdSanPhamCT());
            System.out.println("Quantity after update for ID " + request.getIdSanPhamCT() + ": " + updatedQuantity);
        }

    }





    public List<SanPhamChiTiet> createMultiple(List<SanPhamChiTiet> sanPhamChiTietList, Integer idSanPham) throws IOException {
        for (SanPhamChiTiet spct : sanPhamChiTietList) {
            // Kiểm tra và thiết lập sản phẩm
            SanPham sanPham = new SanPham();
            sanPham.setIdSanPham(idSanPham); // Gán ID sản phẩm từ tham số
            spct.setSanPham(sanPham);
            spct.setTrangThai(true); // Trạng thái là true
            spct.setNgayCapNhat(new Date()); // Ngày cập nhật là ngày hiện tại

            // Lưu chatLieuChiTiet nếu cần
            ChatLieuChiTiet chatLieuChiTiet = spct.getChatLieuChiTiet();
            if (chatLieuChiTiet != null && chatLieuChiTiet.getIdChatLieuChiTiet() != null) {
                chatLieuChiTiet = chatLieuChiTietRepository.findById(chatLieuChiTiet.getIdChatLieuChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("ChatLieuChiTiet không tồn tại"));
            }
            spct.setChatLieuChiTiet(chatLieuChiTiet);

            // Lưu mauSacChiTiet nếu cần
            MauSacChiTiet mauSacChiTiet = spct.getMauSacChiTiet();
            if (mauSacChiTiet != null && mauSacChiTiet.getIdMauSacChiTiet() != null) {
                mauSacChiTiet = mauSacChiTietRepository.findById(mauSacChiTiet.getIdMauSacChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("MauSacChiTiet không tồn tại"));
            }
            spct.setMauSacChiTiet(mauSacChiTiet);

            // Lưu kichThuocChiTiet nếu cần
            KichThuocChiTiet kichThuocChiTiet = spct.getKichThuocChiTiet();
            if (kichThuocChiTiet != null && kichThuocChiTiet.getIdKichThuocChiTiet() != null) {
                kichThuocChiTiet = kichThuocChiTietRepository.findById(kichThuocChiTiet.getIdKichThuocChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("KichThuocChiTiet không tồn tại"));
            }
            spct.setKichThuocChiTiet(kichThuocChiTiet);
        }
        return sanPhamChiTietRepository.saveAll(sanPhamChiTietList);
    }
    @Transactional
    public void muaSanPham(Integer idSanPhamChiTiet, Integer soLuong) {
        int updatedRows = sanPhamChiTietRepository.updateSoLuong(idSanPhamChiTiet, soLuong);
        if (updatedRows == 0) {
            throw new IllegalArgumentException("Số lượng yêu cầu vượt quá số lượng tồn kho hoặc sản phẩm không tồn tại!");
        }
    }
    public void save(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
    public SanPhamChiTiet findById(int id) {
        return sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với ID: " + id));
    }

    public void updateProductQuantity(int idSanPhamChiTiet, int soLuong) {
        SanPhamChiTiet productDetail = findById(idSanPhamChiTiet);
        productDetail.setSoLuong(productDetail.getSoLuong() + soLuong);
        save(productDetail);
    }
}
