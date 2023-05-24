Lab03: Xây dựng chương trình Chat ở như Case Study 2 nhưng client sẽ gọi hàm(qua cơ chế RMI từ server) để lấy danh sách các client đã từng đăng nhập vào server.

a) Hãy làm chức năng bất kỳ client nào đăng nhập vào thì server cũng báo cho nó danh sách các nick đã đăng nhập trước. Cụ thể hơn: client đăng nhập xong thì sẽ gọi hàm RMI của server để lấy danh sách đăng nhập

b) Bất kỳ client nào đăng nhập thì server cũng báo cho các client còn lại (đã đăng nhập trước) về sự có mặt của client mới

c) Bất kỳ client nào đăng xuất thì server cũng xoá khỏi danh sách các client đã đăng nhập và báo cho các client khác.

Gợi ý cách làm:

Đầu tiên SV có thể nạp vào các code cũ của Case Study 2 tại: 
https://drive.google.com/drive/folders/1YZukOpEQcXLAl337Ih7dijtA0788cfDd?usp=sharing

a) Lưu danh sách vừa đăng nhập
B1: đổi tên thuộc tính list của lớp ServerControl thành tên khác: listActiveAccounts. Thêm thuộc tính availableAccounts cũng là một danh sách các tài khoản đã login.

B2: Thêm vào danh sách availableAccounts một user vừa đăng nhập thành công. Thực hiện việc kiểm tra liệu user đó đã đăng nhập chưa? Nếu đã từng đăng nhập rồi thì không cho đăng nhập. Kiểm tra đăng nhập đó thực hiện qua việc bổ sung vào phương thức checkLogin

B3: xây dựng phương thức getAvailableUsers trả về ArrayList<String> là danh sách các User đã đăng nhập vào hệ thống

B4: Xây dựng cơ chế RMI để server đăng ký đối tượng RMI, client gọi đến đối tượng đó để lấy ra danh sách các nick đã đăng nhập

B5: Client sau khi đăng nhập thành công thì sẽ triệu gọi đối tượng availUsers từ RMIRegistry rồi tiếp tục gọi phương thức getAllAvailableUsers.
