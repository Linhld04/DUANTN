package com.example.duantn.query;

public class SanPhamQuery {
    public static final String BASE_QUERY = "SELECT \n" +
            "    sp.Id_san_pham AS idSanPham, \n" +
            "    sp.ten_san_pham AS tenSanPham, \n" +
            "    sp.trang_thai AS trangThai, \n" +
            "    sp.gia_ban AS giaBan,       -- Giá bán trung bình\n" +
            "    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm\n" +
            "    dc.ten_danh_muc AS tenDanhMuc, \n" +
            "    SUM(spct.so_luong) AS soLuong, \n" +
            "    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh\n" +
            "    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa\n" +
            "FROM \n" +
            "    san_pham sp \n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "WHERE \n" +
            "    hl.thu_tu = 1 AND sp.trang_thai = 1 \n" +  // Thêm điều kiện trạng thái
            "GROUP BY \n" +
            "    sp.Id_san_pham, \n" +
            "    sp.ten_san_pham, \n" +
            "    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY\n" +
            "    dc.ten_danh_muc,\n" +
            "    sp.gia_ban,\n" +
            "    spct.so_luong\n" +
            "ORDER BY \n" +
            "    sp.Id_san_pham ASC;";


    public static final String GET_SAN_PHAM_BY_DANH_MUC = "SELECT \n" +
            "    sp.Id_san_pham AS idSanPham, \n" +
            "    sp.ten_san_pham AS tenSanPham, \n" +
            "    sp.trang_thai AS trangThai, \n" +
            "    sp.gia_ban AS giaBan,       -- Giá bán trung bình\n" +
            "    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm\n" +
            "    dc.ten_danh_muc AS tenDanhMuc, \n" +
            "    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh\n" +
            "    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa\n" +
            "FROM \n" +
            "    san_pham sp \n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "WHERE \n" +
            "    hl.thu_tu = 1 AND sp.trang_thai = 1 AND dc.Id_danh_muc = :idDanhMuc\n" +  // Thêm điều kiện trạng thái
            "GROUP BY \n" +
            "    sp.Id_san_pham, \n" +
            "    sp.ten_san_pham, \n" +
            "    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY\n" +
            "    sp.gia_ban,  -- Đảm bảo trường này có trong GROUP BY\n" +
            "    dc.ten_danh_muc;";


    public static final String GET_SAN_PHAM_BY_ID = "SELECT " +
            "    sp.Id_san_pham AS idSanPham, " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    spct.so_luong AS soLuong, " +
            "    spct.mo_ta AS mo_ta_spct, " +
            "    sp.gia_ban AS giaBan, " +
            "    dc.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    hl.trang_thai AS trangThai, " +
            "    ms.ten_mau_sac AS tenMauSac, " +
            "    kc.ten_kich_thuoc AS tenKichThuoc, " +
            "    cl.ten_chat_lieu AS tenChatLieu, " +
            "    hl.url_anh AS urlAnh, " +
            "    hl.thu_tu AS thuTu " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "LEFT JOIN hinh_anh_san_pham hl ON spct.Id_san_pham_chi_tiet = hl.id_san_pham_chi_tiet " +
            "LEFT JOIN chat_lieu_chi_tiet clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN kich_thuoc kc ON kct.id_kich_thuoc = kc.Id_kich_thuoc " +
            "LEFT JOIN mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet " +
            "LEFT JOIN mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac " +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc " +
            "LEFT JOIN loai_voucher lv ON sp.id_loai_voucher = lv.id_loai_voucher " +
            "WHERE " +
            "    sp.Id_san_pham = :idSanPham";


    public static final String ADD_SAN_PHAM_AD =
            "INSERT INTO san_pham (ten_san_pham, gia_ban, mo_ta, id_danh_muc, ngay_tao, ngay_cap_nhat, trang_thai) " +
                    "VALUES (:tenSanPham, :giaBan, :moTa, :idDanhMuc, :ngayTao, :ngayCapNhat, :trangThai); " +
                    "SELECT SCOPE_IDENTITY();";



    public static final String ADD_HINH_ANH_SAN_PHAM_AD = "INSERT INTO hinh_anh_san_pham (id_san_pham, url_anh, thu_tu, loai_hinh_anh) VALUES (:idSanPham, :urlAnh, :thuTu, :loaiHinhAnh);";


    public static final String GET_SAN_PHAM_AD = "SELECT \n" +
            "    sp.id_san_pham AS idSanPham,\n" +
            "    sp.ten_san_pham AS tenSanPham,\n" +
            "    sp.gia_ban AS giaBan,\n" +
            "    sp.mo_ta AS moTa,\n" +
            "    dm.ten_danh_muc AS tenDanhMuc,\n" +
            "    sp.trang_thai AS trangThai\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc\n" +
            "ORDER BY \n" +
            "    sp.ngay_cap_nhat DESC;";

}
