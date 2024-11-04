window.BanHangController = function ($scope, $http) {
    $scope.invoiceToDelete = null;
    $scope.currentPage = 1;
    $scope.pageChanged = function (newPage) {
        $scope.currentPage = newPage;
    };

    $scope.getUsers = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/nguoi-dung')
            .then(function (response) {
                console.log('Dữ liệu người dùng:', response.data);
                $scope.users = response.data;
                $scope.filteredUsers = $scope.users; // Khởi tạo danh sách đã lọc
                $scope.searchTerm = "";
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu người dùng:', error);
            });
    };
    $scope.filterUsers = function () {
        if (!$scope.searchTerm) {
            $scope.filteredUsers = [];
            $scope.showDropdown = false;
        } else {
            // Lọc danh sách người dùng theo từ khóa
            $scope.filteredUsers = $scope.users.filter(function (user) {
                return user.tenNguoiDung.toLowerCase().includes($scope.searchTerm.toLowerCase()) ||
                    user.sdtNguoiDung.includes($scope.searchTerm);
            });
            $scope.showDropdown = true; // Hiển thị dropdown khi có từ khóa
        }
    };

    $scope.selectUser = function (user) {
        $scope.searchTerm = user.tenNguoiDung; // Hiển thị tên người dùng vào ô tìm kiếm
        $scope.selectedUser = user;
        $scope.showDropdown = false; // Ẩn dropdown khi người dùng đã chọn
    };

    $scope.nvs = [];
    $scope.getNV = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/nguoi-dung/ban-hang')
            .then(function (response) {
                $scope.selectedEmployeeName = $scope.nvs.tenNguoiDung;
                console.log('Dữ liệu người dùng:', response.data);
                $scope.nvs = response.data;
            })

            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu người dùng:', error);
            });
    };
    $scope.newCustomer = {
        tenNguoiDung: '',
        sdtNguoiDung: ''
    };
    $scope.openAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = true;
    };
    $scope.closeAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = false;
        $scope.newCustomer.tenNguoiDung = '';
        $scope.newCustomer.sdtNguoiDung = '';
    };
    $scope.addCustomer = function () {
        if ($scope.newCustomer.tenNguoiDung && $scope.newCustomer.sdtNguoiDung) {
            let customerData = {
                tenNguoiDung: $scope.newCustomer.tenNguoiDung,
                sdtNguoiDung: $scope.newCustomer.sdtNguoiDung,
            };

            $http.post('http://localhost:8080/api/nguoi-dung/add', customerData)
                .then(function (response) {
                    console.log('Khách hàng đã được thêm:', response.data);
                    $scope.getUsers();
                    $scope.closeAddCustomerModal();
                    alert("Khách hàng đã được thêm thành công!");
                })
                .catch(function (error) {
                    console.error('Đã xảy ra lỗi:', error);
                    alert("Đã xảy ra lỗi khi thêm khách hàng!");
                });
        } else {
            alert("Vui lòng nhập đầy đủ thông tin khách hàng!");
        }
    };
    $scope.getUnpaidInvoices = function () {
        $http.get('http://localhost:8080/api/hoa-don/chua-thanh-toan')
            .then(function (response) {
                console.log('Danh sách hóa đơn chưa thanh toán:', response.data);
                $scope.unpaidInvoices = response.data;
                if ($scope.unpaidInvoices.length > 5) {
                    $scope.unpaidInvoices = $scope.unpaidInvoices.slice(0, 5);
                    alert("Bạn chỉ được phép tạo tối đa 5 hóa đơn chưa thanh toán!");
                }
            })

    };
    $scope.createInvoice = function () {
        console.log("Selected User:", $scope.selectedUser);

        let newInvoice = {
            tenNguoiNhan: $scope.selectedUser.tenNguoiDung,
            nguoiDung: { id: $scope.selectedUser.id },
            nhanVien: { id: $scope.nvs.id },
            ngayTao: new Date()
        };

        $http.post('http://localhost:8080/api/hoa-don/create', newInvoice)
            .then(function (response) {
                console.log('Hóa đơn được tạo:', response.data);
                $scope.getUnpaidInvoices();
                alert("Hóa đơn đã được tạo thành công!");
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo hóa đơn:', error);
                alert("Có lỗi xảy ra khi tạo hóa đơn!");
            });
    };

    $scope.openDeleteModal = function (invoiceId) {
        $scope.invoiceToDelete = invoiceId;
    };

    $scope.confirmDelete = function () {
        if ($scope.invoiceToDelete) {
            $scope.deleteInvoice($scope.invoiceToDelete);
            $scope.invoiceToDelete = null;
        }
    };
    $scope.deleteInvoice = function (invoiceId) {
        console.log('ID hóa đơn cần xóa:', invoiceId);
        if (invoiceId) {
            $http.delete('http://localhost:8080/api/hoa-don/delete/' + invoiceId)
                .then(function (response) {
                    console.log('Hóa đơn đã được xóa:', response.data);
                    $scope.getUnpaidInvoices();
                    alert("Hóa đơn đã được xóa thành công!");
                })
                .catch(function (error) {
                    console.error('Lỗi khi xóa hóa đơn:', error);
                    alert("Có lỗi xảy ra khi xóa hóa đơn!");
                });
        }
    };

    $scope.getProductDetails = function () {
        $http.get('http://localhost:8080/san-pham-chi-tiet/all')
            .then(function (response) {
                $scope.sanPhamChiTietList = response.data;
                console.log('Danh sách sản phẩm chi tiết:', $scope.sanPhamChiTietList);
            })
            .catch(function (error) {
                console.error("Có lỗi xảy ra khi gọi API sản phẩm chi tiết:", error);
            });
    };
    $scope.selectInvoice = function (invoice) {
        console.log("Hóa đơn đã chọn:", invoice);
        $scope.selectedInvoice = invoice;
        if (!invoice || !invoice.idHoaDon) {
            console.error('Hóa đơn không hợp lệ:', invoice);
            alert("Hóa đơn không hợp lệ!");
            return;
        }
        $http.get('http://localhost:8080/api/hoa-don/' + invoice.idHoaDon)
            .then(function (response) {
                console.log('Chi tiết hóa đơn:', response.data);
                $scope.selectedInvoice = response.data;
                $scope.selectedInvoice.maHoaDon = response.data.maHoaDon;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
            });
    };

    $scope.calculateChange = function () {
        if ($scope.tienKhachDua !== undefined && !isNaN($scope.tienKhachDua)) {
            $scope.tienThua = Math.max($scope.tienKhachDua - $scope.totalPrice, 0);
        } else {
            $scope.tienThua = 0;
        }
        console.log("Tiền thừa:", $scope.tienThua);
    };

    // add giỏ hàng theo id người dùng
    $scope.addToCart = function (idSanPhamChiTiet) {
        let cartItem = {
            idSanPhamChiTiet: idSanPhamChiTiet,
            soLuong: 1
        };
        $http.post('http://localhost:8080/api/gio-hang/them/' + $scope.selectedUser.id, cartItem)
            .then(function (response) {
                console.log('người dùng đã thêm giỏ hàng: ', $scope.selectedUser.id)
                alert("Sản phẩm đã được thêm vào giỏ hàng!");
                $scope.getCartItems();
                $scope.getProductDetails();
            })

    };
    // lấy danh sách sp theo id người dùng đã thêm vào giỏ hàng
    $scope.getCartItems = function () {
        if (!$scope.selectedUser || !$scope.selectedUser.id) {
            alert("Vui lòng chọn người dùng để xem giỏ hàng.");
            return;
        }
        $http.get('http://localhost:8080/api/gio-hang/lay/' + $scope.selectedUser.id)
            .then(function (response) {
                console.log('Danh sách sản phẩm trong giỏ hàng:', response.data);
                $scope.cartItems = response.data;
                $scope.calculateTotalPrice();
                $scope.getProductDetails() // sau khi lấy thêm giỏ hàng thì load lại spct nhé kkk
                // Gọi getVoucher() ngay khi bạn cần, ví dụ khi giỏ hàng thay đổi
                $scope.getVoucher();

            })
            .catch(function (error) {
                console.error('Lỗi khi lấy giỏ hàng:', error);
                alert("Có lỗi xảy ra khi lấy giỏ hàng!");
            });
    };


    $scope.calculateTotalPrice = function () {
        $scope.totalPrice = 0;

        // Tính tổng tiền hàng từ giỏ
        if ($scope.cartItems && Array.isArray($scope.cartItems)) {
            $scope.cartItems.forEach(function (item) {
                if (item && item.soLuong && item.giaBan) {
                    $scope.totalPrice += item.soLuong * item.giaBan;
                }
            });
        } else {
            console.error('Giỏ hàng không hợp lệ:', $scope.cartItems);
        }
        // Đảm bảo tổng tiền không âm
        $scope.totalPrice = Math.max($scope.totalPrice, 0);
        $scope.getVoucher();

    };
    $scope.getVoucher = function () {
        // Thay đổi đường dẫn API để sử dụng PathVariable
        $http.get('http://localhost:8080/api/voucher/tuong_ung/' + $scope.totalPrice)
            .then(function (response) {
                if (response.data) {
                    $scope.selectedVoucher = response.data;
                    $scope.calculateTotalPriceAfterDiscount();
                } else {
                    alert("Không tìm thấy voucher phù hợp.");
                }
            })
    };    
    
    $scope.calculateTotalPriceAfterDiscount = function () {
        if ($scope.selectedVoucher) {
            let discount = $scope.selectedVoucher.gia_tri_giam_gia; // % giảm giá
            let minAmount = $scope.selectedVoucher.soTienToiThieu; // Số tiền tối thiểu để áp dụng voucher
    
            if ($scope.totalPrice >= minAmount) {
                $scope.totalPrice -= ($scope.totalPrice * discount / 100);
                // Kiểm tra giá trị tối đa
                if ($scope.totalPrice > $scope.selectedVoucher.giaTriToiDa) {
                    $scope.totalPrice = $scope.selectedVoucher.giaTriToiDa;
                }
            } 
        }
    };
    
    // Khi xóa sản phẩm khỏi giỏ hàng, bạn cũng có thể muốn tính toán lại tổng tiền
    $scope.deleteCartItem = function (productId) {
        const userId = $scope.selectedUser.id;
        $http.delete('http://localhost:8080/api/gio-hang/xoa-san-pham/' + userId + '/' + productId)
            .then(function (response) {
                console.log('Sản phẩm đã được xóa khỏi giỏ hàng:', response.data);
                alert("Sản phẩm đã được xóa thành công khỏi giỏ hàng!");
                $scope.getCartItems(); // Load lại giỏ hàng
                $scope.getProductDetails(); // Load lại sản phẩm
                $scope.calculateTotalPrice(); // Tính toán lại tổng tiền sau khi xóa sản phẩm
                $scope.getVoucher();
            })
            .catch(function (error) {
                console.error('Lỗi khi xóa sản phẩm khỏi giỏ hàng:', error);
                alert("Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng!");
            });
    };

    $scope.deleteCart = function (productId) {
        const userId = $scope.selectedUser.id;
        $http.delete('http://localhost:8080/api/gio-hang/xoa-tat-ca/' + userId)
    };
    $scope.updateQuantity = function (item) {
        const userId = $scope.selectedUser.id;
        const updatedQuantity = { soLuong: item.soLuong };

        $http.put('http://localhost:8080/api/gio-hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
            .then(function (response) {
                console.log('Cập nhật số lượng thành công:', response.data);
                $scope.calculateTotalPrice();
                $scope.getProductDetails(); // Load lại sản phẩm
            })
            .catch(function (error) {
                console.error('Lỗi khi cập nhật số lượng:', error);
                alert("Có lỗi xảy ra khi cập nhật số lượng!");
            });
    };


    $scope.getPhuongThucThanhToan = function () {
        $http.get('http://localhost:8080/api/phuong-thuc-thanh-toan/ten-phuong-thuc')
            .then(function (response) {
                console.log('Dữ liệu phương thức thanh toán:', response.data);
                $scope.phuongThucThanhToan = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu phương thức thanh toán:', error);
            });
    };


    $scope.createHoaDonChiTiet = function () {
        $scope.getCartItems();
        $scope.hoaDonChiTietDTO = [];
        $scope.cartItems.forEach(function (item) {
            const detail = {
                idHoaDon: $scope.selectedInvoice.idHoaDon,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                soLuong: item.soLuong,
                tongTien: item.giaBan * item.soLuong,
                soTienThanhToan: parseFloat($scope.tienKhachDua),
                tienTraLai: $scope.tienThua,
                moTa: item.moTa || ''
            };
            $scope.hoaDonChiTietDTO.push(detail);
        });
        $http.post("http://localhost:8080/api/hoa-don-chi-tiet/create", $scope.hoaDonChiTietDTO, {
            params: { userId: $scope.selectedUser.id }
        })
            .then(function (response) {
                alert("Thanh toán thành công");
                console.log("Hóa đơn chi tiết:", response.data);
                $scope.deleteGioHangChiTietByUserId();
                $scope.updateInvoiceStatus();
                $scope.createPaymentMethod();
                $scope.deleteCart();
            })
            .catch(function (error) {
                console.error("Lỗi khi tạo hóa đơn chi tiết:", error);
                alert("Có lỗi xảy ra. Vui lòng thử lại.");
            });
    };
    $scope.deleteGioHangChiTietByUserId = function () {
        const userId = $scope.selectedUser.id;
        $http.delete("http://localhost:8080/api/gio-hang/delete/user/" + userId)
            .then(function (response) {
                $scope.getCartItems();
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa chi tiết giỏ hàng:", error);
            });
    };


    $scope.updateInvoiceStatus = function () {
        if (!$scope.selectedInvoice || !$scope.selectedInvoice.idHoaDon) {
            alert("Vui lòng chọn hóa đơn cần cập nhật!");
            return;
        }
        $http.put("http://localhost:8080/api/hoa-don/" + $scope.selectedInvoice.idHoaDon + "/update-status", { trangThai: true })
            .then(function (response) {
                alert("trạng thái hóa đơn thành công!");
                console.log("Kết quả cập nhật:", response.data);
            })
    };
    $scope.createPaymentMethod = function () {
        var paymentMethod = {
            phuongThucThanhToan: { id: $scope.selectedPhuongThucThanhToan },
            soTienThanhToan: Number($scope.customerPaid),
            ngayGiaoDich: new Date().toISOString(),
            ghiChu: "Thanh toán hóa đơn " + $scope.selectedInvoice.maHoaDon
        };
        $http.post("http://localhost:8080/api/hoa-don/" + $scope.selectedInvoice.idHoaDon + "/thanh-toan", paymentMethod)
            .then(function (response) {
                console.log("Phản hồi từ POST:", response.data);
                if (response.data && response.data.id) {
                    var updatedInvoice = {
                        phiShip: 0,
                        ngayThanhToan: new Date().toISOString(),
                        thanhTien: Number($scope.totalPrice),
                        idPtThanhToanHoaDon: response.data.id
                    };
                    console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                    return $http.put("http://localhost:8080/api/hoa-don/cap-nhat/" + $scope.selectedInvoice.idHoaDon, updatedInvoice)
                        .then(function (putResponse) {
                            console.log("Hóa đơn đã được cập nhật thành công:", putResponse.data);
                            alert("Hóa đơn đã được cập nhật thành công!");
                        })
                        .catch(function (error) {
                            console.error("Có lỗi xảy ra khi cập nhật hóa đơn:", error);
                            alert("Có lỗi xảy ra khi cập nhật hóa đơn. Vui lòng thử lại.");
                        });
                } else {
                    console.error("Không tìm thấy ID từ phản hồi thanh toán.");
                    alert("Có lỗi xảy ra khi tạo phương thức thanh toán. Vui lòng thử lại.");
                }
            })

    };
    $scope.printInvoice = function () {
        // Tạo một phần tử mới để in hóa đơn
        var printContents = document.getElementById('invoicePrintModal').innerHTML;

        // Tạo cửa sổ pop-up mới
        var popupWin = window.open('', '_blank', 'width=600,height=600');
        popupWin.document.open();

        // Ghi nội dung hóa đơn vào cửa sổ pop-up với CSS để định dạng
        popupWin.document.write(`
                <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                color: #333;
                                margin: 20px;
                            }
                            .store-info {
                                text-align: center;
                                margin-bottom: 20px;
                            }
                            .store-info h4 {
                                margin: 0;
                                font-weight: bold;
                            }
                            .store-info p {
                                margin: 5px 0;
                            }
                            h6 {
                                font-weight: bold;
                                margin-bottom: 10px;
                            }
                            table {
                                width: 100%;
                                border-collapse: collapse;
                                margin-bottom: 20px;
                            }
                            th, td {
                                border: 1px solid #333;
                                padding: 10px;
                                text-align: left;
                            }
                            th {
                                background-color: #f2f2f2;
                            }
                            .total-label {
                                font-weight: 600;
                                margin-top: 15px;
                            }
                        </style>
                    </head>
                    <body onload="window.print()">
                        ${printContents}
                    </body>
                </html>
            `);

        popupWin.document.close();
    };
    $scope.openDiscountModal = function () {
        $('#discountModal').modal('show');
    };

    $scope.customerPaid = null;
    $scope.getProductDetails();
    $scope.getUsers();
    $scope.getNV();
    $scope.getUnpaidInvoices();
    $scope.deleteInvoice();
    $scope.getPhuongThucThanhToan();
    $scope.getVoucher(); // Gọi hàm để lấy voucher

}