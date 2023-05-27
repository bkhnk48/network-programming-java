Lab04: 

Xây dựng chương trình Chat ở như Lab03 nhưng server sẽ phải kiểm tra các tài khoản đã có trên hệ thống bằng cách quản lý CSDL

a) Hãy làm chức năng bất kỳ client nào đăng nhập vào thì server cũng báo cho nó danh sách các nick đã đăng nhập trước. Cụ thể hơn: client đăng nhập xong thì sẽ gọi hàm RMI của server để lấy danh sách đăng nhập

b) Bất kỳ client nào đăng nhập thì server cũng báo cho các client còn lại (đã đăng nhập trước) về sự có mặt của client mới

c) Bất kỳ client nào đăng xuất thì server cũng xoá khỏi danh sách các client đã đăng nhập và báo cho các client khác.
