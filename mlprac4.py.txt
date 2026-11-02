# ===== Cell 1: Import libraries =====
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans, AgglomerativeClustering
from scipy.cluster.hierarchy import dendrogram, linkage
%matplotlib inline


# ===== Cell 2: Load dataset =====
df = pd.read_csv("sales_data_sample.csv", encoding="latin1")
print(df.shape)
df.head()


# ===== Cell 3: Select numeric columns and check missing values =====
cols = ["QUANTITYORDERED", "PRICEEACH", "SALES", "MSRP"]
X = df[cols]
print(X.isnull().sum())


# ===== Cell 4: Detect outliers using boxplot =====
X.plot(kind="box", subplots=True, layout=(1, 4), figsize=(12, 4))
plt.show()


# ===== Cell 5: Remove outliers using IQR method =====
for col in cols:
    Q1 = df[col].quantile(0.25)
    Q3 = df[col].quantile(0.75)
    IQR = Q3 - Q1
    lower = Q1 - 1.5 * IQR
    upper = Q3 + 1.5 * IQR
    df = df[(df[col] >= lower) & (df[col] <= upper)]

df = df.reset_index(drop=True)
X = df[cols]
print("Shape after removing outliers:", df.shape)

X.plot(kind="box", subplots=True, layout=(1, 4), figsize=(12, 4))
plt.show()


# ===== Cell 6: Scale the data =====
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)


# ===== Cell 7: Elbow Method to find number of clusters =====
wcss = []
for k in range(1, 11):
    km = KMeans(n_clusters=k, random_state=42, n_init=10)
    km.fit(X_scaled)
    wcss.append(km.inertia_)

plt.plot(range(1, 11), wcss, marker="o")
plt.title("Elbow Method")
plt.xlabel("Number of clusters (K)")
plt.ylabel("WCSS")
plt.show()


# ===== Cell 8: K-Means with K = 3 (elbow point) =====
kmeans = KMeans(n_clusters=3, random_state=42, n_init=10)
df["KMeans_Cluster"] = kmeans.fit_predict(X_scaled)
print(df["KMeans_Cluster"].value_counts())

plt.scatter(df["QUANTITYORDERED"], df["SALES"], c=df["KMeans_Cluster"], cmap="viridis")
plt.title("K-Means Clusters")
plt.xlabel("Quantity Ordered")
plt.ylabel("Sales")
plt.show()


# ===== Cell 9: Hierarchical Clustering - Dendrogram =====
Z = linkage(X_scaled, method="ward")
plt.figure(figsize=(10, 5))
dendrogram(Z, truncate_mode="lastp", p=30)
plt.title("Dendrogram")
plt.xlabel("Data points")
plt.ylabel("Euclidean distance")
plt.show()


# ===== Cell 10: Agglomerative Clustering with 3 clusters =====
hc = AgglomerativeClustering(n_clusters=3,