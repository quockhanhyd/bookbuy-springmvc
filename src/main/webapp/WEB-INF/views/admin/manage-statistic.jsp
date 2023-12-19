<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<script>document.title = 'Thống kê | Book Buy'</script>
<div class="app__container">
    <div class="app-content__linker">
        <div class="grid wide">
            <ul class="app-content__linker-wrapper">
                <li><a href="<c:url value='/home' />"><span class="material-icons-outlined">home</span>Trang chủ</a></li>
                <li><a href="<c:url value='/admin/home' />">/ Quản trị</a></li>
                <li><a href="#">/ Quản lý thông tin sách</a></li>
            </ul>
        </div>
    </div>
    <div class="grid wide">
        <div class="row">
            <div class="col l-3 m-12 c-12">
                <div class="app-container__menu">
                    <div class="app-container-menu__user">
                        <div class="app-container-menu-user__img">
                            <img src="<c:url value='/template/images/user.png' />" alt="user-image">
                        </div>
                        <div class="app-container-menu-user__info">
                            <div class="app-container-menu-user-info__name">Nguyễn Quốc Khánh</div>
                            <div class="app-container-menu-user-info__email">quockhanh01091@gmail.com</div>
                        </div>
                    </div>
                    <ul class="app-container-menu__control">
                        <li class="app-container-menu-control__item active-color">
                            <a href="<c:url value='/admin/manage-book' />">
                                <span class="material-icons-outlined">book</span>
                                Quản lý thông tin sách
                            </a>
                        </li>
                        <li class="app-container-menu-control__item">
                            <a href="<c:url value='/admin/manage-category' />">
                                <span class="material-icons-outlined">category</span>
                                Quản lý danh mục sách
                            </a>
                        </li>
                        <li class="app-container-menu-control__item">
                            <a href="<c:url value='/admin/manage-purchase' />">
                                <span class="material-icons-outlined">inventory</span>
                                Quản lý đơn đặt hàng
                            </a>
                        </li>
                        <li class="app-container-menu-control__item" id="tab-admin">
                            <a href="<c:url value='/admin/manage-user-info' />">
                                <span class="material-icons-outlined">manage_accounts</span>
                                Quản lý thông tin người dùng
                            </a>
                        </li>
                        <li class="app-container-menu-control__item">
                            <a href="<c:url value='/chat' />">
                                <span class="material-icons-outlined">question_answer</span>
                                Chat với khách hàng
                            </a>
                        </li>
                        <li class="app-container-menu-control__item">
                            <a href="<c:url value='/admin/statistic' />">
                                <span class="material-symbols-outlined">monitoring</span>
                                Thống kê
                            </a>
                        </li>
                        <li class="app-container-menu-control__item">
                            <a href="#">
                                <span class="material-icons-outlined">info</span>
                                Thông tin cửa hàng
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col l-9 m-12 c-12">
                <div class="app-container__content">
                	<div class="app-container-content__text">Thống kê</div>
                	<div class="app-header__search">
                		<label for="from-date">Từ ngày:</label>
						<input type="date" id="from-date" name="from-date" style="width: 100px; border: 1px solid black;">
						
						<label for="to-date" style="margin-left: 8px">Đến ngày:</label>
						<input type="date" id="to-date" name="to-date" style="width: 100px; border: 1px solid black;">
						
						<label for="type" style="margin-left: 8px">Theo:</label>
						<select id="type" name="type" style="width: 100px; border: 1px solid black;">
						  <option value="1">Ngày</option>
						  <option value="2">Tháng</option>
						  <option value="3">Quý</option>
						  <option value="4">Năm</option>
						</select>
						
						<div class="app__btn" style="margin-left: 8px; height: 100%; line-height: 30px;" onclick="getData()">Tìm kiếm</div>
                	</div>
                    <div style="display: flex">
						<div id="piechart" style="width: 50%; height: 500px;"></div>
						<div id="chart_div" style="width: 100%; height: 500px;"></div>
					</div>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="<c:url value='/template/js/main_base_admin.js' />"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="<c:url value='/template/js/main_manage_statistic.js' />"></script>