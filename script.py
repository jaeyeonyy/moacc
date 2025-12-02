import mysql.connector
from faker import Faker
from datetime import datetime, timedelta
import random

# =======================================================
# 1. DB 연결 정보 및 시뮬레이션 설정
# =======================================================
DB_CONFIG = {
    'host': 'localhost',
    'database': 'moacc',
    'user': 'root',
    'password': '12345678'
}

# 시뮬레이션 설정
TOTAL_DAYS = 1000          
# 🚨 수정됨: 하루 1건 주문만 생성 🚨
ORDERS_PER_DAY = 10000       

# FK 참조 설정
# 🚨 수정됨: USER_ID = 3 🚨
USER_ID = 1
SKU_ID_MIN = 2
SKU_ID_MAX = 165

# ENUM 값 고정 
ORDER_STATUS = 'PAID'
PAYMENT_METHOD = '간편결제'
# 🚨 수정됨: ENUM 오류 해결을 위해 'SUCCESS' 사용 🚨
PAYMENT_STATUS = 'DONE'
# =======================================================


def run_data_generation():
    """DB에 연결하여 1,000건의 더미 주문 데이터를 생성하고 삽입합니다."""
    
    # DB 연결
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        print("DB 연결 성공.")
    except mysql.connector.Error as err:
        print(f"DB 연결 실패: {err}")
        return

    # Faker 초기화
    fake = Faker('ko_KR')
    
    # =======================================================
    # 2. SKU 데이터 조회 (가격 및 Product ID 참조용)
    # =======================================================
    print(f"SKU (ID: {SKU_ID_MIN}~{SKU_ID_MAX}) 데이터 조회 중...")
    
    sku_query = f"SELECT id, price, product_id FROM product_skus WHERE id BETWEEN {SKU_ID_MIN} AND {SKU_ID_MAX}"
    cursor.execute(sku_query)
    
    sku_data = {}
    for row in cursor.fetchall():
        sku_data[row['id']] = {
            'price': float(row['price']),
            'product_id': row['product_id']
        }
        
    if not sku_data:
        print(f"오류: product_skus 테이블에 ID {SKU_ID_MIN}~{SKU_ID_MAX} 범위의 데이터가 없습니다. 스크립트를 종료합니다.")
        cursor.close()
        conn.close()
        return
    
    available_sku_ids = list(sku_data.keys())
    print(f"유효 SKU {len(available_sku_ids)}개 로드 완료. 데이터 생성 시작.")
    
    # =======================================================
    # 3. 데이터 생성 루프
    # =======================================================
    
    start_date = datetime.now() - timedelta(days=TOTAL_DAYS - 1)
    
    # 🚨 수정됨: ID 시작값 설정 🚨
    initial_order_id = 102800
    initial_order_item_id = 205000
    
    order_id_counter = initial_order_id
    order_item_id_counter = initial_order_item_id
    
    orders_batch = []
    order_items_batch = []
    payments_batch = []

    try:
        for day_offset in range(TOTAL_DAYS):
            current_date = start_date + timedelta(days=day_offset)
            # date_str은 배치 삽입 출력에 사용
            date_str = current_date.strftime('%Y-%m-%d')
            
            for _ in range(ORDERS_PER_DAY):
                
                # 3-A. Order 데이터 생성
                order_time = fake.date_time_between(
                    start_date=current_date.replace(hour=0, minute=0, second=0), 
                    end_date=current_date.replace(hour=23, minute=59, second=59)
                ).strftime('%Y-%m-%d %H:%M:%S')
                
                num_items = random.randint(1, 3)
                current_order_total = 0
                
                # 3-B. Order Item 데이터 생성
                selected_skus = set(random.choices(available_sku_ids, k=num_items))
                
                for sku_id in selected_skus:
                    sku_info = sku_data[sku_id]
                    quantity = random.randint(1, 5)
                    item_price = sku_info['price'] * quantity
                    current_order_total += item_price

                    # order_item 데이터 튜플 구성
                    order_items_batch.append((
                        order_item_id_counter, # order_item_id (PK)
                        order_time,        # created_at
                        order_time,        # modified_at
                        sku_info['price'],   # price (단가)
                        f"상품명-{sku_info['product_id']}", # product_name 
                        quantity,            
                        order_id_counter,      # order_id (FK)
                        sku_id,              # sku_id (FK)
                    ))
                    order_item_id_counter += 1
                
                
                # Order 테이블 데이터 튜플 구성
                orders_batch.append((
                    order_id_counter,  # order_id (PK)
                    order_time,        # created_at
                    order_time,        # modified_at
                    f"주문-{order_id_counter}", # order_name
                    ORDER_STATUS,      # order_status (ENUM)
                    fake.address(),    # shipping_address
                    int(current_order_total), # total_amount
                    USER_ID,           # user_id (3)
                    None,              # toss_order_id 
                ))

                # 3-C. Payment 데이터 생성
                payments_batch.append((
                    PAYMENT_METHOD,    
                    PAYMENT_STATUS,    
                    int(current_order_total), 
                    f"payment_{order_id_counter}", 
                    order_id_counter,  
                    order_time,        
                ))
                
                order_id_counter += 1
                
            # 배치 삽입 (1000건마다 배치 삽입)
            if len(orders_batch) >= 1000:
                print(f"날짜: {date_str} - {len(orders_batch)}건 배치 삽입 중... (총 {TOTAL_DAYS}건)")
                
                insert_batch(cursor, orders_batch, 'orders')
                insert_batch(cursor, order_items_batch, 'order_item')
                insert_batch(cursor, payments_batch, 'payment')
                
                conn.commit()
                
                orders_batch = []
                order_items_batch = []
                payments_batch = []

        # 남아있는 데이터 최종 삽입
        if orders_batch:
            print(f"남은 {len(orders_batch)}건 최종 배치 삽입 중...")
            insert_batch(cursor, orders_batch, 'orders')
            insert_batch(cursor, order_items_batch, 'order_item')
            insert_batch(cursor, payments_batch, 'payment')
            conn.commit()

        final_order_count = order_id_counter - initial_order_id
        
        print("\n==============================================")
        print(f"✅ 총 {final_order_count}건의 주문 데이터 생성 및 삽입 완료!")
        print(f"✅ orders ID는 {initial_order_id}부터, order_item ID는 {initial_order_item_id}부터 시작했습니다.")
        print("==============================================")

    except Exception as e:
        print(f"\n❌ 데이터 생성 중 오류 발생: {e}")
        conn.rollback()
        
    finally:
        cursor.close()
        conn.close()
        print("DB 연결 종료.")


def insert_batch(cursor, data, table_name):
    """지정된 테이블에 데이터를 배치 삽입합니다. DDL에 따라 컬럼 순서를 정확히 반영했습니다."""
    
    if table_name == 'orders':
        # DDL: order_id, created_at, modified_at, order_name, order_status, shipping_address, total_amount, user_id, toss_order_id
        sql = """
        INSERT INTO orders 
        (order_id, created_at, modified_at, order_name, order_status, shipping_address, total_amount, user_id, toss_order_id) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
    elif table_name == 'order_item':
        # DDL: order_item_id, created_at, modified_at, price, product_name, quantity, order_id, sku_id
        sql = """
        INSERT INTO order_item 
        (order_item_id, created_at, modified_at, price, product_name, quantity, order_id, sku_id) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        """
    elif table_name == 'payment':
        # payment DDL은 없으므로 이전 구조 유지
        sql = """
        INSERT INTO payment 
        (method, payment_status, amount, payment_key, order_id, approved_at) 
        VALUES (%s, %s, %s, %s, %s, %s)
        """
    else:
        return

    cursor.executemany(sql, data)


if __name__ == '__main__':
    run_data_generation()