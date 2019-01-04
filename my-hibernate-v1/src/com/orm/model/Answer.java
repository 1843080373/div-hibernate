package com.orm.model;

import java.io.Serializable;
import java.util.Date;

import com.orm.core.enums.Column;
import com.orm.core.enums.Entity;
import com.orm.core.enums.Id;
import com.orm.core.enums.Table;
@Entity
@Table(name="t_answer")
public class Answer implements Serializable {
	
	@Id(name="id",db_type="int",generator="native")
    private Integer id;
	@Column(name="create_time",db_type="datetime")
    private Date createTime;
	@Column(name="enabled",db_type="bit")
    private Boolean enabled;
	@Column(name="update_time",db_type="datetime")
    private Date updateTime;
	@Column(name="version",db_type="int")
    private Integer version;
	@Column(name="user_id",db_type="int")
    private Integer userId;
	@Column(name="questionnaire_id",db_type="int")
    private Integer questionnaireId;
	@Column(name="choice_id",db_type="int")
    private Integer choiceId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }
}