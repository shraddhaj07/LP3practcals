
# 1. Import Libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

from google.colab import files
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error

# 2. Upload Dataset
uploaded = files.upload()
df = pd.read_csv(next(iter(uploaded)))

print("Dataset Preview:")
print(df.head())

print("\nDataset Information:")
print(df.info())

# 3. Data Preprocessing
print("\nMissing Values:")
print(df.isnull().sum())

df = df.dropna().copy()
df = df.drop_duplicates()

# Remove unnecessary columns
df = df.drop(columns=["Unnamed: 0", "key"], errors="ignore")

# Convert pickup_datetime into datetime
df["pickup_datetime"] = pd.to_datetime(
    df["pickup_datetime"], errors="coerce", utc=True
)

df = df.dropna(subset=["pickup_datetime"])

# Extract date and time features
df["hour"] = df["pickup_datetime"].dt.hour
df["day"] = df["pickup_datetime"].dt.day
df["month"] = df["pickup_datetime"].dt.month
df["year"] = df["pickup_datetime"].dt.year

df = df.drop(columns=["pickup_datetime"])

# Remove invalid values
df = df[
    (df["fare_amount"] > 0) &
    (df["passenger_count"] > 0) &
    (df["pickup_latitude"].between(-90, 90)) &
    (df["dropoff_latitude"].between(-90, 90)) &
    (df["pickup_longitude"].between(-180, 180)) &
    (df["dropoff_longitude"].between(-180, 180))
].copy()

# 4. Identify and Remove Outliers Using IQR
Q1 = df["fare_amount"].quantile(0.25)
Q3 = df["fare_amount"].quantile(0.75)
IQR = Q3 - Q1

lower = Q1 - 1.5 * IQR
upper = Q3 + 1.5 * IQR

plt.figure(figsize=(8, 4))
sns.boxplot(x=df["fare_amount"])
plt.title("Fare Amount Before Outlier Removal")
plt.show()

df = df[
    (df["fare_amount"] >= lower) &
    (df["fare_amount"] <= upper)
].copy()

print("Dataset Shape After Cleaning:", df.shape)

# 5. Correlation Analysis
plt.figure(figsize=(12, 7))
sns.heatmap(df.corr(numeric_only=True),
            annot=True, cmap="coolwarm", fmt=".2f")
plt.title("Correlation Heatmap")
plt.show()

# 6. Feature Selection
X = df.drop("fare_amount", axis=1)
y = df["fare_amount"]

# Split Dataset
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# 7. Linear Regression
lr = LinearRegression()
lr.fit(X_train, y_train)

y_pred_lr = lr.predict(X_test)

# 8. Random Forest Regression
rf = RandomForestRegressor(
    n_estimators=100, random_state=42, n_jobs=-1
)
rf.fit(X_train, y_train)

y_pred_rf = rf.predict(X_test)

# 9. Model Evaluation
def evaluate_model(name, y_true, y_pred):
    r2 = r2_score(y_true, y_pred)
    rmse = np.sqrt(mean_squared_error(y_true, y_pred))
    mae = mean_absolute_error(y_true, y_pred)

    print("\n", name)
    print("R2 Score:", r2)
    print("RMSE:", rmse)
    print("MAE:", mae)

    return [name, r2, rmse, mae]

lr_result = evaluate_model(
    "Linear Regression", y_test, y_pred_lr
)

rf_result = evaluate_model(
    "Random Forest Regression", y_test, y_pred_rf
)

# 10. Compare Models
results = pd.DataFrame(
    [lr_result, rf_result],
    columns=["Model", "R2 Score", "RMSE", "MAE"]
)

print("\nModel Comparison:")
print(results)

# Visualize Model Comparison
results.set_index("Model")[["RMSE", "MAE"]].plot(
    kind="bar", figsize=(8, 5)
)
plt.title("Model Error Comparison")
plt.ylabel("Error")
plt.xticks(rotation=0)
plt.show()

# 11. Actual vs Predicted Fare
plt.figure(figsize=(8, 5))
plt.scatter(y_test, y_pred_lr, alpha=0.3,
            label="Linear Regression")
plt.scatter(y_test, y_pred_rf, alpha=0.3,
            label="Random Forest")
plt.xlabel("Actual Fare")
plt.ylabel("Predicted Fare")
plt.title("Actual vs Predicted Uber Fare")
plt.legend()
plt.show()
