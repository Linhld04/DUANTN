package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SPBanHangQuayDTO {
    private Long idSanPham;
    private String maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private String moTa;
    private String tenDanhMuc;
    private String tenDotGiamGia;
    private BigDecimal giaKhuyenMai;
    private BigDecimal giaTriGiamGia;
    private boolean kieuGiamGia;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String urlAnh;
    private Integer thuTu;

}
