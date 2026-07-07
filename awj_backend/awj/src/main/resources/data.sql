INSERT INTO awj_user (username, password, phone, real_name, role, status) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13800138000', '管理员', 'ADMIN', 1),
('test', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13800138001', '测试用户', 'USER', 1),
('merchant1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13800138002', '商家张三', 'MERCHANT', 1);

INSERT INTO awj_merchant (user_id, name, logo, address, phone, description, status, audit_time) VALUES
(3, '社区便利店', 'https://example.com/logo.png', '北京市朝阳区XX街道XX号', '13800138002', '提供各类生活用品和食品', 1, NOW());

INSERT INTO awj_category (name, parent_id, type, sort_order, status) VALUES
('食品饮料', 0, 'PRODUCT', 1, 1),
('日用百货', 0, 'PRODUCT', 2, 1),
('生鲜蔬果', 0, 'PRODUCT', 3, 1),
('家政服务', 0, 'SERVICE', 1, 1),
('维修服务', 0, 'SERVICE', 2, 1),
('美容美发', 0, 'SERVICE', 3, 1);

INSERT INTO awj_product (merchant_id, category_id, name, description, image, total_stock, price, original_price, status, sales) VALUES
(1, 1, '可口可乐 500ml', '优质碳酸饮料', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coca%20cola%20500ml%20bottle&image_size=square', 100, 3.50, 4.00, 1, 50),
(1, 1, '农夫山泉 550ml', '优质天然矿泉水', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=nongfu%20spring%20water%20bottle&image_size=square', 200, 2.00, 2.50, 1, 100),
(1, 1, '乐事薯片 原味', '经典口味薯片', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=lays%20potato%20chips%20bag&image_size=square', 50, 8.90, 10.00, 1, 30),
(1, 2, '维达纸巾', '高品质纸巾', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=vinda%20tissue%20paper%20pack&image_size=square', 80, 29.90, 35.00, 1, 40),
(1, 1, '蒙牛纯牛奶', '优质纯牛奶', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=mengniu%20milk%20carton&image_size=square', 60, 59.90, 68.00, 1, 25),
(1, 1, '矿泉水 550ml', '优质天然矿泉水，口感清爽', 'https://example.com/product1.jpg', 100, 2.00, NULL, 1, 50),
(1, 1, '方便面 5连包', '经典口味，方便快捷', 'https://example.com/product2.jpg', 50, 15.00, NULL, 1, 30),
(1, 2, '洗衣液 2kg', '深层洁净，持久留香', 'https://example.com/product3.jpg', 30, 25.00, NULL, 1, 20),
(1, 3, '新鲜苹果 500g', '红富士苹果，脆甜可口', 'https://example.com/product4.jpg', 200, 10.00, NULL, 1, 80);

INSERT INTO awj_service (merchant_id, category_id, name, description, image, price, original_price, duration, status, sales) VALUES
(1, 4, '家庭保洁', '专业家庭清洁服务', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=home%20cleaning%20service&image_size=square', 99.00, NULL, 120, 1, 20),
(1, 5, '家电维修', '专业家电维修', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=appliance%20repair%20service&image_size=square', 80.00, NULL, 60, 1, 15),
(1, 6, '理发服务', '专业理发', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=hair%20cut%20service&image_size=square', 58.00, NULL, 45, 1, 30),
(1, 6, '美甲服务', '专业美甲', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=manicure%20service&image_size=square', 98.00, NULL, 90, 1, 25),
(1, 5, '管道疏通', '专业管道疏通', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=plumbing%20service&image_size=square', 60.00, NULL, 30, 1, 10),
(1, 4, '家庭保洁服务', '专业保洁团队，深度清洁', 'https://example.com/service1.jpg', 150.00, NULL, 120, 1, 10),
(1, 4, '衣物清洗服务', '干洗水洗，专业护理', 'https://example.com/service2.jpg', 50.00, NULL, 480, 1, 25),
(1, 5, '家电维修服务', '专业维修师傅上门服务', 'https://example.com/service3.jpg', 80.00, NULL, 60, 1, 15);

INSERT INTO awj_banner (title, image, link, sort_order, status) VALUES
('夏日清凉', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=summer%20cool%20drinks%20banner%20design&image_size=landscape_16_9', '/pages/category/category', 1, 1),
('限时特惠', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=limited%20time%20sale%20banner%20design&image_size=landscape_16_9', '/pages/category/category', 2, 1),
('新人专享优惠', 'https://example.com/banner1.jpg', '/pages/coupon/coupon', 3, 1),
('服务到家活动', 'https://example.com/banner3.jpg', '/pages/service/list', 4, 1);

INSERT INTO awj_coupon (name, amount, min_amount, start_time, end_time) VALUES
('新人优惠券', 10.00, 50.00, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY)),
('满减优惠', 20.00, 100.00, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));

INSERT INTO awj_address (user_id, receiver_name, receiver_phone, province, city, district, detail, is_default) VALUES
(2, '测试用户', '13800138001', '北京市', '北京市', '朝阳区', 'XX街道XX小区XX号楼XX室', 1);
