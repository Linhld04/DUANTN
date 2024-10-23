package com.example.duantn.repository;

import com.example.duantn.dto.ThongKeDoanhThuDTO;
import com.example.duantn.entity.SanPham;
import com.example.duantn.query.SanPhamQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = SanPhamQuery.BASE_QUERY, nativeQuery = true)
    List<Object[]> getAllSanPham();

    @Query(value = SanPhamQuery.GET_SAN_PHAM_BY_DANH_MUC, nativeQuery = true)
    List<Object[]> getSanPhamByDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);

    @Query(value = SanPhamQuery.GET_SAN_PHAM_BY_ID, nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPham") String idSanPham);

    @Query(value = SanPhamQuery.GET_SAN_PHAM_AD, nativeQuery = true)
    List<Object[]> getAllSanPhamAD();

    @Modifying
    @Transactional
    @Query(value = SanPhamQuery.ADD_SAN_PHAM_AD, nativeQuery = true)
    Integer addSanPham(@Param("idDanhMuc") Integer idDanhMuc,
                       @Param("tenSanPham") String tenSanPham,
                       @Param("giaBan") BigDecimal giaBan,
                       @Param("moTa") String moTa,
                       @Param("ngayTao") Date ngayTao,
                       @Param("ngayCapNhat") Date ngayCapNhat,
                       @Param("trangThai") Boolean trangThai);

    @Modifying
    @Transactional
    @Query(value = SanPhamQuery.ADD_HINH_ANH_SAN_PHAM_AD, nativeQuery = true)
    void addHinhAnhSanPham(@Param("idSanPham") Integer idSanPham,
                           @Param("urlAnh") String urlAnh,
                           @Param("thuTu") Integer thuTu,
                           @Param("loaiHinhAnh") String loaiHinhAnh);

    @Query("SELECT s.idSanPham FROM SanPham s ORDER BY s.ngayTao DESC")
    List<Integer> getLatestSanPhamId();


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM HinhAnhSanPham h WHERE h.sanPham.idSanPham = :idSanPham")
    void deleteHinhAnhBySanPhamId(@Param("idSanPham") Integer idSanPham);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SanPhamChiTiet s WHERE s.sanPham.idSanPham = :idSanPham")
    void deleteChiTietBySanPhamId(@Param("idSanPham") Integer idSanPham);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SanPham s WHERE s.idSanPham = :idSanPham")
    void deleteSanPhamByIdSanPham(@Param("idSanPham") Integer idSanPham);

    @Modifying
    @Query(value = "UPDATE san_pham " +
            "SET Trang_thai = CASE WHEN Trang_thai = 1 THEN 0 ELSE 1 END, " +
            "ngay_cap_nhat = GETDATE() " +
            "WHERE Id_san_pham = :idSanPham", nativeQuery = true)
    void updateStatusById(@Param("idSanPham") Integer idSanPham);



}
