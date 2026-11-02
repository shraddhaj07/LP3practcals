
# 1. Import Libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

from google.colab import files
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.svm import SVC
from sklearn.metrics import (accuracy_score, precision_score,
                             recall_score, f1_score,
                             confusion_matrix, classification_report)

# 2. Upload Dataset
uploaded = files.upload()
df = pd.read_csv(next(iter(uploaded)))

print("Dataset Preview:")
print(df.head())

print("\nDataset Shape:", df.shape)

# 3. Data Preprocessing
print("\nMissing Values:")
print(df.isnull().sum())

df = df.dropna()
df = df.drop_duplicates()

# Remove unnecessary column
df = df.drop(columns=["Email No."], errors="ignore")

# 4. Separate Features and Target
X = df.drop("Prediction", axis=1)
y = df["Prediction"]

# 0 = Not Spam, 1 = Spam
print("\nClass Distribution:")
print(y.value_counts())

# 5. Split Dataset
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)

# 6. Feature Scaling
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 7. K-Nearest Neighbors
knn = KNeighborsClassifier(n_neighbors=5)
knn.fit(X_train, y_train)

y_pred_knn = knn.predict(X_test)

# 8. Support Vector Machine
svm = SVC(kernel="linear", random_state=42)
svm.fit(X_train, y_train)

y_pred_svm = svm.predict(X_test)

# 9. Model Evaluation
def evaluate_model(name, y_true, y_pred):
    accuracy = accuracy_score(y_true, y_pred)
    precision = precision_score(y_true, y_pred, zero_division=0)
    recall = recall_score(y_true, y_pred, zero_division=0)
    f1 = f1_score(y_true, y_pred, zero_division=0)

    print("\n", name)
    print("Accuracy:", accuracy)
    print("Precision:", precision)
    print("Recall:", recall)
    print("F1 Score:", f1)

    print("\nClassification Report:")
    print(classification_report(y_true, y_pred,
                                zero_division=0))

    # Confusion Matrix
    cm = confusion_matrix(y_true, y_pred, labels=[0, 1])

    sns.heatmap(cm, annot=True, fmt="d", cmap="Blues",
                xticklabels=["Not Spam", "Spam"],
                yticklabels=["Not Spam", "Spam"])
    plt.title(name + " Confusion Matrix")
    plt.xlabel("Predicted")
    plt.ylabel("Actual")
    plt.show()

    return [name, accuracy, precision, recall, f1]

knn_result = evaluate_model("KNN", y_test, y_pred_knn)
svm_result = evaluate_model("SVM", y_test, y_pred_svm)

# 10. Compare Models
results = pd.DataFrame(
    [knn_result, svm_result],
    columns=["Model", "Accuracy", "Precision", "Recall", "F1 Score"]
)

print("\nModel Comparison:")
print(results)

# 11. Visualize Model Comparison
results.set_index("Model").plot(
    kind="bar", figsize=(9, 5)
)
plt.title("KNN vs SVM Performance")
plt.ylabel("Score")
plt.ylim(0, 1)
plt.xticks(rotation=0)
plt.legend(loc="lower right")
plt.show()

# 12. Identify Best Model
best_model = results.loc[results["F1 Score"].idxmax(), "Model"]
print("\nBest Model Based on F1 Score:", best_model)
