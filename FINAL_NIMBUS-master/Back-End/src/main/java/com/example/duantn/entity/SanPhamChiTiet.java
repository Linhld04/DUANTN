package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_san_pham_chi_tiet")
    private Integer idSanPhamChiTiet;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "id_kich_thuoc_chi_tiet", referencedColumnName = "Id_kich_thuoc_chi_tiet")
    private KichThuocChiTiet kichThuocChiTiet;

    @ManyToOne
    @JoinColumn(name = "id_mau_sac_chi_tiet", referencedColumnName = "Id_mau_sac_chi_tiet")
    private MauSacChiTiet mauSacChiTiet;

    @ManyToOne
    @JoinColumn(name = "id_chat_lieu_chi_tiet", referencedColumnName = "Id_chat_lieu_tiet")
    private ChatLieuChiTiet chatLieuChiTiet;

    @ManyToOne
    @JoinColumn(name = "Id_san_pham", referencedColumnName = "Id_san_pham")
    private SanPham sanPham;

}
