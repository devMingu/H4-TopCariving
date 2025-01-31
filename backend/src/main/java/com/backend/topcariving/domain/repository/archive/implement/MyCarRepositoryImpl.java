package com.backend.topcariving.domain.repository.archive.implement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.backend.topcariving.domain.dto.option.response.estimation.OptionSummaryDTO;
import com.backend.topcariving.domain.entity.archive.MyCar;
import com.backend.topcariving.domain.entity.option.enums.Category;
import com.backend.topcariving.domain.entity.option.enums.CategoryDetail;
import com.backend.topcariving.domain.repository.archive.MyCarRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MyCarRepositoryImpl implements MyCarRepository {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Optional<MyCar> findById(final Long myCarId) {
		String sql = "SELECT * FROM MY_CAR WHERE my_car_id = ?";

		final List<MyCar> result = jdbcTemplate.query(sql, myCarRowMapper(), myCarId);
		if (result.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(result.get(0));
	}

	@Override
	public MyCar save(MyCar myCar) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName("MY_CAR").usingGeneratedKeyColumns("my_car_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("car_option_id", myCar.getCarOptionId());
		parameters.put("archiving_id", myCar.getArchivingId());

		Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
		return MyCar.builder()
			.myCarId(key.longValue())
			.carOptionId(myCar.getCarOptionId())
			.archivingId(myCar.getArchivingId())
			.build();
	}

	@Override
	public Optional<MyCar> findByArchivingIdAndCarOptionId(Long archivingId, Long carOptionId) {
		String sql = "SELECT * FROM MY_CAR WHERE archiving_id = ? AND car_option_id = ?;";
		List<MyCar> results = jdbcTemplate.query(sql, myCarRowMapper(), archivingId, carOptionId);

		if (results.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(results.get(0));
	}

	@Override
	public List<MyCar> findByArchivingId(Long archivingId) {
		String sql = "SELECT * FROM MY_CAR WHERE archiving_id = ?;";
		return jdbcTemplate.query(sql, myCarRowMapper(), archivingId);
	}

	@Override
	public List<OptionSummaryDTO> findOptionSummaryByArchivingId(final Long archivingId) {
		String sql = "SELECT MC.car_option_id, option_name, category, category_detail, price, photo_url "
			+ "FROM MY_CAR AS MC INNER JOIN CAR_OPTION AS CO ON MC.car_option_id = CO.car_option_id "
			+ "WHERE archiving_id = ?;";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new OptionSummaryDTO(
			rs.getLong("car_option_id"),
			rs.getString("option_name"),
			Category.valueOfName(rs.getString("category")),
			CategoryDetail.valueOfName(rs.getString("category_detail")),
			rs.getInt("price"),
			rs.getString("photo_url"),
			null
		), archivingId);
	}

	@Override
	public void deleteByArchivingIdAndCategoryDetail(final Long archivingId, final CategoryDetail categoryDetail) {
		String sql = "DELETE FROM MY_CAR WHERE car_option_id IN (SELECT car_option_id FROM CAR_OPTION WHERE CATEGORY_DETAIL = ?) AND archiving_id = ?;";

		jdbcTemplate.update(sql, categoryDetail.getName(), archivingId);
	}

	@Override
	public void saveMultipleData(final List<MyCar> myCars) {
		String sql = "INSERT INTO MY_CAR (car_option_id, archiving_id) VALUES (?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException {
				final MyCar myCar = myCars.get(i);
				if (myCar.getCarOptionId() == null) {
					ps.setNull(1, Types.BIGINT);
				} else {
					ps.setLong(1, myCar.getCarOptionId());
				}
				ps.setLong(2, myCar.getArchivingId());
			}

			@Override
			public int getBatchSize() {
				return myCars.size();
			}
		});
	}

	@Override
	public void updateCarOptionIdByArchivingIdAndCategoryDetail(final Long archivingId, final Long carOptionId, final CategoryDetail categoryDetail) {
		String sql = "UPDATE MY_CAR SET car_option_id = ? "
			+ "WHERE archiving_id = ? AND "
			+ "car_option_id IN (SELECT car_option_id FROM CAR_OPTION WHERE category_detail = ?)";

		jdbcTemplate.update(sql, carOptionId, archivingId, categoryDetail.getName());
	}

	@Override
	public List<MyCar> findByCategoryDetailAndArchivingId(CategoryDetail categoryDetail, Long archivingId) {
		String sql = "SELECT * FROM MY_CAR MC INNER JOIN CAR_OPTION AS CO ON CO.car_option_id = MC.car_option_id"
			+ " WHERE category_detail = ? AND archiving_id = ?";

		return jdbcTemplate.query(sql, myCarRowMapper(), categoryDetail.getName(), archivingId);
	}

	private RowMapper<MyCar> myCarRowMapper() {
		return (rs, rowNum) ->
			MyCar.builder()
				.myCarId(rs.getLong("my_car_id"))
				.carOptionId(rs.getObject("car_option_id", Long.class))
				.archivingId(rs.getLong("archiving_id"))
				.build();
	}
}
