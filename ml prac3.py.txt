# K-Nearest Neighbours on diabetes.csv

import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import confusion_matrix, accuracy_score, precision_score, recall_score

# 1. Load dataset
df = pd.read_csv("diabetes.csv")
print(df.head())

# 2. Handle missing values (0 is not possible in these columns)
#    Replace them with the median instead of removing rows
cols = ["Glucose", "BloodPressure", "SkinThickness", "Insulin", "BMI"]
for c in cols:
    df[c] = df[c].replace(0, df[c].median())

# 3. Treat outliers using IQR method (cap them, don't remove rows)
features = df.columns.drop("Outcome")
for c in features:
    Q1 = df[c].quantile(0.25)
    Q3 = df[c].quantile(0.75)
    IQR = Q3 - Q1
    lower = Q1 - 1.5 * IQR
    upper = Q3 + 1.5 * IQR
    df[c] = df[c].clip(lower, upper)

# 4. Separate features (X) and target (y)
X = df.drop("Outcome", axis=1)
y = df["Outcome"]

# 5. Split into training (80%) and testing (20%) data
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 6. Scale the features (KNN uses distance)
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 7. Create and train KNN model
knn = KNeighborsClassifier(n_neighbors=5)
knn.fit(X_train, y_train)

# 8. Predict on test data
y_pred = knn.predict(X_test)

# 9. Evaluation
cm = confusion_matrix(y_test, y_pred)
accuracy = accuracy_score(y_test, y_pred)
error_rate = 1 - accuracy
precision = precision_score(y_test, y_pred)
recall = recall_score(y_test, y_pred)

print("\nConfusion Matrix:\n", cm)
print("Accuracy   :", round(accuracy, 4))
print("Error Rate :", round(error_rate, 4))
print("Precision  :", round(precision, 4))
print("Recall     :", round(recall, 4))