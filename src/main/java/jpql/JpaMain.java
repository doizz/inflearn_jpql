package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);


            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m ").getResultList();

            MemberDTO memberDTO = result.get(0);

            tx.commit();
        } catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }
}


/**
 * - JPQL 문법
 *  - select m from Member as m where m.age>18
 *  - 엔티티와 속성은 대소문자 구분 O
 *  - JPQL키워드는 대소문자 구분 X
 *  - 엔티티 이름 사용, 테이블 이름이 아님
 *  - 별칭은 필수
 *
 * - 결과조회 API
 *  - query.getResultList() : 결과가 하나 이상일 때, 리스트 반환 ( 결과가 없으면 빈 리스트 반환)
 *  - query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
 *   - 결과가 없으면 : javax.persistence.NoResultException
 *   - 결과가 둘 이상이면 : javax.persistence.NonUniqueResultException
 *
 *
 * - 프로젝션
 *  - select 절에 조회할대상을지정 하는 것
 *  - 프로덕션대상: 엔티티,임베디드 타입, 스칼라 타입
 *  - select m from Member m -> 엔티티 프로젝션
 *  - select m.team from Member m -> 엔티티 프로젝션
 *  - select m.address from Member m -> 임베디드 타입 프로젝션
 *  - select m.username , m.age fromm Member m -> 스칼라 타입프로젝션
 *  - distinct로 중복 제거
 *
 * - 프로젝션 - 여러 값 조회
 *  - select m.username, m.age from Member m
 *
 *  - 1. Query 타입으로 조회
 *  - 2. Object[] 타입으로 조회
 *  - 3. new 명령어로 조회
 *   - 단순값을 DTO로 바로 조회
 *   - select new MemberDTO from Member m
 *   - 패키지 명을 포함한 전체 클래스 명 입력
 *   - 순서와 타입이 일치하는 생성자 필요
 *
 *
 */