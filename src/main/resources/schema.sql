-- Identity and Access Management
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(36) PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    account_status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    role_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS permissions (
    permission_id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS credentials (
    credential_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    hash_algorithm VARCHAR(50),
    verified BOOLEAN DEFAULT FALSE,
    last_password_change TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sessions (
    session_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    session_token VARCHAR(255) NOT NULL UNIQUE,
    session_status VARCHAR(50),
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    revoked_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    reset_token_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP,
    used_at TIMESTAMP,
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audit_logs (
    audit_log_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    event_type VARCHAR(100),
    event_description VARCHAR(500),
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    occurred_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(36),
    role_id VARCHAR(36),
    assigned_at TIMESTAMP,
    assigned_by VARCHAR(36),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id VARCHAR(36),
    permission_id VARCHAR(36),
    granted_at TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

-- IoT Monitoring
CREATE TABLE IF NOT EXISTS devices (
    id VARCHAR(36) PRIMARY KEY,
    hardware_mac_id VARCHAR(100) UNIQUE,
    organization_id VARCHAR(36) NOT NULL,
    campus_id VARCHAR(36),
    campus_name VARCHAR(255),
    room_id VARCHAR(36),
    name VARCHAR(255),
    ip_address VARCHAR(45),
    status VARCHAR(50),
    last_connection TIMESTAMP,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sensors (
    id VARCHAR(36) PRIMARY KEY,
    device_id VARCHAR(36),
    name VARCHAR(255),
    type VARCHAR(100),
    is_active BOOLEAN,
    created_at TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS sensor_readings (
    id VARCHAR(36) PRIMARY KEY,
    sensor_id VARCHAR(36) NOT NULL,
    value DECIMAL(10, 4),
    unit VARCHAR(50),
    recorded_at TIMESTAMP,
    FOREIGN KEY (sensor_id) REFERENCES sensors(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS support_tickets (
    ticket_id VARCHAR(36) PRIMARY KEY,
    ticket_number VARCHAR(50) UNIQUE,
    client_name VARCHAR(255),
    category VARCHAR(100),
    priority VARCHAR(50),
    device_id VARCHAR(36),
    technician_id VARCHAR(36),
    issue_description TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    resolved_at TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
);

-- Machine Learning Automation
CREATE TABLE IF NOT EXISTS ml_models (
    id VARCHAR(36) PRIMARY KEY,
    model_name VARCHAR(255),
    version VARCHAR(50),
    accuracy DECIMAL(5, 4),
    deployed BOOLEAN,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS predictions (
    id VARCHAR(36) PRIMARY KEY,
    model_id VARCHAR(36),
    input_data TEXT,
    predicted_value DECIMAL(10, 4),
    confidence_score DECIMAL(5, 4),
    status VARCHAR(50),
    created_at TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES ml_models(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS telemetry_data (
    id VARCHAR(36) PRIMARY KEY,
    prediction_id VARCHAR(36),
    sensor_type VARCHAR(100),
    value DECIMAL(10, 4),
    recorded_at TIMESTAMP,
    FOREIGN KEY (prediction_id) REFERENCES predictions(id) ON DELETE SET NULL
);

-- Analytics
CREATE TABLE IF NOT EXISTS metric_cards (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(36),
    title VARCHAR(255),
    value DECIMAL(10, 4),
    unit VARCHAR(50),
    contaminant_type VARCHAR(100),
    status_name VARCHAR(100),
    status_description VARCHAR(255),
    last_reading_text VARCHAR(255),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trend_charts (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(36),
    title VARCHAR(255),
    contaminant_type VARCHAR(100),
    time_range VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS reports (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    room_id VARCHAR(36),
    title VARCHAR(255),
    period VARCHAR(100),
    generated_at TIMESTAMP,
    summary TEXT
);

CREATE TABLE IF NOT EXISTS dashboards (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    room_id VARCHAR(36),
    current_co2_metric_card_id VARCHAR(36),
    current_pm25_metric_card_id VARCHAR(36),
    current_temperature_metric_card_id VARCHAR(36),
    pm25_trend_chart_id VARCHAR(36),
    temperature_trend_chart_id VARCHAR(36),
    co2_trend_chart_id VARCHAR(36),
    last_updated_at TIMESTAMP,
    FOREIGN KEY (current_co2_metric_card_id) REFERENCES metric_cards(id) ON DELETE SET NULL,
    FOREIGN KEY (current_pm25_metric_card_id) REFERENCES metric_cards(id) ON DELETE SET NULL,
    FOREIGN KEY (current_temperature_metric_card_id) REFERENCES metric_cards(id) ON DELETE SET NULL,
    FOREIGN KEY (pm25_trend_chart_id) REFERENCES trend_charts(id) ON DELETE SET NULL,
    FOREIGN KEY (temperature_trend_chart_id) REFERENCES trend_charts(id) ON DELETE SET NULL,
    FOREIGN KEY (co2_trend_chart_id) REFERENCES trend_charts(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS trend_data_points (
    id VARCHAR(36) PRIMARY KEY,
    trend_chart_id VARCHAR(36),
    recorded_at TIMESTAMP,
    value DECIMAL(10, 4),
    FOREIGN KEY (trend_chart_id) REFERENCES trend_charts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS report_metric_cards (
    report_id VARCHAR(36),
    metric_card_id VARCHAR(36),
    PRIMARY KEY (report_id, metric_card_id),
    FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE,
    FOREIGN KEY (metric_card_id) REFERENCES metric_cards(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS report_trend_charts (
    report_id VARCHAR(36),
    trend_chart_id VARCHAR(36),
    PRIMARY KEY (report_id, trend_chart_id),
    FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE,
    FOREIGN KEY (trend_chart_id) REFERENCES trend_charts(id) ON DELETE CASCADE
);

-- Subscription and Billing
CREATE TABLE IF NOT EXISTS organizations (
    organization_id VARCHAR(36) NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT organizations_pk PRIMARY KEY (organization_id)
);

CREATE TABLE IF NOT EXISTS billing_plans (
    plan_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    monthly_price DECIMAL(10,2) NOT NULL,
    yearly_price DECIMAL(10,2) NOT NULL,
    base_included_devices INT NOT NULL,
    additional_device_fee DECIMAL(10,2) DEFAULT 0.00,
    historical_reports_access BOOLEAN DEFAULT FALSE,
    predictive_analytics_access BOOLEAN DEFAULT FALSE,
    automation_enabled BOOLEAN DEFAULT FALSE,
    CONSTRAINT billing_plans_pk PRIMARY KEY (plan_id)
);

CREATE TABLE IF NOT EXISTS subscriptions (
    subscription_id VARCHAR(36) NOT NULL,
    organization_id VARCHAR(36) NOT NULL,
    plan_id VARCHAR(36) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    auto_renew BOOLEAN DEFAULT TRUE,
    suspended_reason VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT subscriptions_pk PRIMARY KEY (subscription_id),
    CONSTRAINT fk_subscription_organization FOREIGN KEY (organization_id) REFERENCES organizations (organization_id),
    CONSTRAINT fk_subscription_plan FOREIGN KEY (plan_id) REFERENCES billing_plans (plan_id)
);

CREATE INDEX IF NOT EXISTS idx_subscription_status ON subscriptions (status);

CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(36) NOT NULL,
    subscription_id VARCHAR(36) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    external_transaction_id VARCHAR(255) NULL,
    CONSTRAINT payments_pk PRIMARY KEY (payment_id),
    CONSTRAINT fk_payment_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions (subscription_id)
);

CREATE INDEX IF NOT EXISTS idx_payment_status ON payments (payment_status);

CREATE TABLE IF NOT EXISTS invoices (
    invoice_id VARCHAR(36) NOT NULL,
    payment_id VARCHAR(36) NOT NULL,
    organization_id VARCHAR(36) NOT NULL,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    invoice_status VARCHAR(50) NOT NULL,
    CONSTRAINT invoices_pk PRIMARY KEY (invoice_id),
    CONSTRAINT fk_invoice_organization FOREIGN KEY (organization_id) REFERENCES organizations (organization_id),
    CONSTRAINT fk_invoice_payment FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
);

CREATE INDEX IF NOT EXISTS idx_invoice_status ON invoices (invoice_status);

-- Notification
CREATE TABLE IF NOT EXISTS alerts (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    severity VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id VARCHAR(36) PRIMARY KEY,
    alert_id VARCHAR(36),
    message VARCHAR(255),
    recipient VARCHAR(255),
    status VARCHAR(50),
    type VARCHAR(50),
    created_at TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES alerts(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS notification_channels (
    id VARCHAR(36) PRIMARY KEY,
    notification_id VARCHAR(36),
    channel_name VARCHAR(100),
    active BOOLEAN,
    FOREIGN KEY (notification_id) REFERENCES notifications(id) ON DELETE CASCADE
);
