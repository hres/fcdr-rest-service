package hc.fcdr.rws.util;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hc.fcdr.rws.config.ResponseCodes;
import hc.fcdr.rws.domain.Product;
import hc.fcdr.rws.domain.Sales;
import hc.fcdr.rws.domain.Package;
import hc.fcdr.rws.model.PackageRequest;
import hc.fcdr.rws.model.PackageResponse;
import hc.fcdr.rws.model.ProductRequest;
import hc.fcdr.rws.model.ProductResponse;
import hc.fcdr.rws.model.SalesRequest;
import hc.fcdr.rws.model.SalesResponse;

/**
 * Utility class for DAO's. This class contains commonly used DAO logic which is been refactored in single static
 * methods. As far it contains a PreparedStatement values setter and several quiet close methods.
 * 
 */
public final class DaoUtil
{

    // Constructors
    // -------------------------------------------------------------------------------

    private DaoUtil()
    {

    }

    // Actions
    // ------------------------------------------------------------------------------------

    /**
     * Returns a PreparedStatement of the given connection, set with the given SQL query and the given parameter values.
     * 
     * @param connection
     *            The Connection to create the PreparedStatement from.
     * @param sql
     *            The SQL query to construct the PreparedStatement with.
     * @param returnGeneratedKeys
     *            Set whether to return generated keys or not.
     * @param values
     *            The parameter values to be set in the created PreparedStatement.
     * @throws SQLException
     *             If something fails during creating the PreparedStatement.
     */
    public static PreparedStatement prepareStatement(Connection connection,
            String sql, boolean returnGeneratedKeys, Object... values)
            throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS
                        : Statement.NO_GENERATED_KEYS);

        if (values != null)
            setValues(preparedStatement, values);

        return preparedStatement;
    }

    /**
     * Set the given parameter values in the given PreparedStatement.
     * 
     * @param connection
     *            The PreparedStatement to set the given parameter values in.
     * @param values
     *            The parameter values to be set in the created PreparedStatement.
     * @throws SQLException
     *             If something fails during setting the PreparedStatement values.
     */
    public static void setValues(PreparedStatement preparedStatement,
            Object... values) throws SQLException
    {
        for (int i = 0; i < values.length; i++)
            preparedStatement.setObject(i + 1, values[i]);
    }

    /**
     * Converts the given java.util.Date to java.sql.Date.
     * 
     * @param date
     *            The java.util.Date to be converted to java.sql.Date.
     * @return The converted java.sql.Date.
     */
    public static Date toSqlDate(java.util.Date date)
    {
        return (date != null) ? new Date(date.getTime()) : null;
    }

    /**
     * Quietly close the Connection. Any errors will be printed to the stderr.
     * 
     * @param connection
     *            The Connection to be closed quietly.
     */
    public static void close(Connection connection)
    {
        if (connection != null)
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                System.err.println(
                        "Closing Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Quietly close the Statement. Any errors will be printed to the stderr.
     * 
     * @param statement
     *            The Statement to be closed quietly.
     */
    public static void close(Statement statement)
    {
        if (statement != null)
            try
            {
                statement.close();
            }
            catch (SQLException e)
            {
                System.err.println(
                        "Closing Statement failed: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Quietly close the ResultSet. Any errors will be printed to the stderr.
     * 
     * @param resultSet
     *            The ResultSet to be closed quietly.
     */
    public static void close(ResultSet resultSet)
    {
        if (resultSet != null)
            try
            {
                resultSet.close();
            }
            catch (SQLException e)
            {
                System.err.println(
                        "Closing ResultSet failed: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Quietly close the Connection and Statement. Any errors will be printed to the stderr.
     * 
     * @param connection
     *            The Connection to be closed quietly.
     * @param statement
     *            The Statement to be closed quietly.
     */
    public static void close(Connection connection, Statement statement)
    {
        close(statement);
        close(connection);
    }

    /**
     * Quietly close the Connection, Statement and ResultSet. Any errors will be printed to the stderr.
     * 
     * @param connection
     *            The Connection to be closed quietly.
     * @param statement
     *            The Statement to be closed quietly.
     * @param resultSet
     *            The ResultSet to be closed quietly.
     */
    public static void close(Connection connection, Statement statement,
            ResultSet resultSet)
    {
        close(resultSet);
        close(statement);
        close(connection);
    }

    /**
     * Quietly close Statement and ResultSet. Any errors will be printed to the stderr.
     * 
     * @param statement
     *            The Statement to be closed quietly.
     * @param resultSet
     *            The ResultSet to be closed quietly.
     */
    public static void close(Statement statement, ResultSet resultSet)
    {
        close(resultSet);
        close(statement);
    }

    /**
     * Returns the Long value corresponding to the given field on the given ResultSet. If the value cannot be parsed to
     * Long, or does not exist, it returns a null value.
     * 
     * @param rs
     *            ResultSet
     * @param field
     *            Field we want to obtain the value from
     * @return Long value if the field exists and can be parsed to Long. Null otherwise.
     */
    public static Long getLongFromResultSet(ResultSet rs, String field)
    {

        Long result = null;

        try
        {
            Object value = rs.getObject(field);
            if (value != null)
                result = (Long) value;
        }
        catch (Exception e)
        {
        }
        return result;
    }

    /**
     * Returns a string list corresponding to the given field on the given ResultSet. If the value cannot be parsed to
     * an array, or does not exist, it returns an empty value.
     * 
     * @param rs
     *            ResultSet
     * @param field
     *            Field we want to obtain the value from
     * @return Long value if the field exists and can be parsed to Long. Null otherwise.
     */
    public static List<String> getArrayFromResultSet(ResultSet rs, String field)
    {

        List<String> result;

        try
        {
            Array arrayChunks = rs.getArray(field);
            String[] chunks = (String[]) arrayChunks.getArray();
            result = Arrays.asList(chunks);

            if (result.contains(null))
                result = new ArrayList<String>();
        }
        catch (Exception e)
        {
            result = new ArrayList<String>();
        }

        return result;
    }

    public static Integer getIntFromResultSet(ResultSet rs, String field)
    {

        Integer result = null;

        try
        {
            Object value = rs.getObject(field);
            if (value != null)
                result = (Integer) value;
        }
        catch (Exception e)
        {
        }
        return result;
    }

    public static Product getProduct(ResultSet result) throws SQLException
    {

        Product product = new Product();

        product.setId(result.getLong("product_id"));
        product.setDescription(result.getString("product_description"));
        product.setBrand(result.getString("product_brand"));
        product.setCountry(result.getString("product_country"));
        product.setClusterNumber(result.getDouble("cluster_number"));
        product.setComment(result.getString("product_comment"));
        product.setManufacturer(result.getString("product_manufacturer"));
        product.setCnfCode(result.getInt("cnf_code"));
        product.setCreationDate(result.getTimestamp("creation_date"));
        product.setLastEditDate(result.getTimestamp("last_edit_date"));
        product.setEditedBy(result.getString("edited_by"));

        return product;
    }

    public static ProductResponse getProductResponse(ResultSet resultSet)
            throws SQLException
    {
        Product product = getProduct(resultSet);
        ProductResponse productResponse = new ProductResponse(product);

        productResponse.setClassification_number(
                resultSet.getDouble("classification_number"));
        productResponse.setClassification_name(
                resultSet.getString("classification_name"));
        productResponse.setClassification_type(
                resultSet.getString("classification_type"));

        return productResponse;
    }

    public static Map<String, Object> getQueryMap(ProductRequest request)
    {
        Map<String, Object> queryMap = new HashMap<String, Object>();

        if (!request.product_manufacturer.isEmpty())
            queryMap.put("product_manufacturer", request.product_manufacturer);
        if (!request.product_brand.isEmpty())
            queryMap.put("product_brand", request.product_brand);

        if (request.cnf_code != null)
        {
            if (isType(request.cnf_code.toString(), "int"))
            {
                if (request.cnf_code > 0)
                    queryMap.put("cnf_code", request.cnf_code);
            }
            else
                queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);

        if (request.cluster_number != null)
        {
            if (isType(request.cluster_number.toString(), "double"))
            {
                if (request.cluster_number > 0.0)
                    queryMap.put("cluster_number", request.cluster_number);
            }
            else
                queryMap.put("inputError", ResponseCodes.INVALID_DOUBLE);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_DOUBLE);

        if (!request.product_description.isEmpty())
            queryMap.put("product_description", request.product_description);
        if (!request.product_comment.isEmpty())
            queryMap.put("product_comment", request.product_comment);

        if (request.classification_number != null)
        {
            if (isType(request.classification_number.toString(), "double"))
            {
                if (request.classification_number > 0.0)
                    queryMap.put("classification_number",
                            request.classification_number);
            }
            else
                queryMap.put("inputError", ResponseCodes.INVALID_DOUBLE);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_DOUBLE);

        if (!request.classification_name.isEmpty())
            queryMap.put("classification_name", request.classification_name);
        if (!request.classification_type.isEmpty())
            queryMap.put("classification_type", request.classification_type);

        if (request.offset != null)
        {
            if (!isType(request.offset.toString(), "int"))
                queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);

        if (!request.restaurant_type.isEmpty())
            queryMap.put("restaurant_type", request.restaurant_type);
        if (!request.type.isEmpty())
            queryMap.put("type", request.type);
        
        return queryMap;
    }

    // ===

    public static Sales getSales(ResultSet result) throws SQLException
    {
        Sales sales = new Sales();

        sales.setId(result.getLong("sales_id"));
        sales.setDescription(result.getString("sales_description"));
        sales.setUpc(result.getString("sales_upc"));
        sales.setBrand(result.getString("sales_brand"));
        sales.setManufacturer(result.getString("sales_manufacturer"));
        sales.setDollarRank(result.getDouble("dollar_rank"));
        sales.setDollarVolume(result.getDouble("dollar_volume"));
        sales.setDollarShare(result.getDouble("dollar_share"));
        sales.setDollarVolumePercentageChange(
                result.getDouble("dollar_volume_percentage_change"));
        sales.setKiloVolume(result.getDouble("kilo_volume"));
        sales.setKiloShare(result.getDouble("kilo_share"));
        sales.setKiloVolumePercentageChange(
                result.getDouble("kilo_volume_percentage_change"));
        sales.setAverageAcDist(result.getDouble("average_ac_dist"));
        sales.setAverageRetailPrice(result.getDouble("average_retail_price"));
        sales.setSalesSource(result.getString("sales_source"));
        sales.setNielsenCategory(result.getString("nielsen_category"));
        sales.setSalesYear(result.getInt("sales_year"));
        sales.setControlLabelFlag(result.getBoolean("control_label_flag"));
        sales.setKiloVolumeTotal(result.getDouble("kilo_volume_total"));
        sales.setKiloVolumeRank(result.getDouble("kilo_volume_rank"));
        sales.setDollarVolumeTotal(result.getDouble("dollar_volume_total"));
        sales.setClusterNumber(result.getDouble("cluster_number"));
        sales.setProductGrouping(result.getDouble("product_grouping"));
        sales.setSalesProductDescription(
                result.getString("sales_product_description"));
        sales.setClassificationNumber(
                result.getDouble("classification_number"));
        sales.setClassificationType(result.getString("classification_type"));
        sales.setSalesComment(result.getString("sales_comment"));
        sales.setSalesCollectionDate(result.getDate("sales_collection_date"));
        sales.setNumberOfUnits(result.getInt("number_of_units"));
        sales.setCreationDate(result.getTimestamp("creation_date"));
        sales.setLastEditDate(result.getTimestamp("last_edit_date"));
        sales.setEditedBy(result.getString("edited_by"));
        sales.setProductId(result.getLong("sales_product_id_fkey"));

        return sales;
    }

    public static SalesResponse getSalesResponse(ResultSet resultSet)
            throws SQLException
    {
        Sales sales = getSales(resultSet);
        SalesResponse salesResponse = new SalesResponse(sales);

        return salesResponse;
    }

    public static Map<String, Object> getQueryMap(SalesRequest request)
    {
        Map<String, Object> queryMap = new HashMap<String, Object>();

        if (!request.salesUpc.isEmpty())
            if (StringUtilities.isNumeric(request.salesUpc))
                queryMap.put("sales_upc", request.salesUpc);
            else
                queryMap.put("inputError", ResponseCodes.INVALID_UPC);
        if (!request.salesDescription.isEmpty())
            queryMap.put("sales_description", request.salesDescription);
        if (!request.salesSource.isEmpty())
            queryMap.put("sales_source", request.salesSource);

        if (request.salesYear != null)
        {
            if (isType(request.salesYear.toString(), "int"))
            {
                if (request.salesYear > 0)
                    queryMap.put("sales_year", request.salesYear);
            }
            else
                queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);

        if (!request.nielsenCategory.isEmpty())
            queryMap.put("nielsen_category", request.nielsenCategory);
        if (!request.salesComment.isEmpty())
            queryMap.put("sales_comment", request.salesComment);

        if (DateUtil.validateDates(request.collectionDateFrom,
                request.collectionDateTo))
        {
            if (!request.collectionDateFrom.isEmpty()
                    && !request.collectionDateTo.isEmpty())
            {
                queryMap.put("collection_date_from",
                        request.collectionDateFrom);
                queryMap.put("collection_date_to", request.collectionDateTo);
            }
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_DATE);

        if (request.offset != null)
        {
            if (!isType(request.offset.toString(), "int"))
                queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);

        return queryMap;
    }

    // ===

    public static Package getPackage(ResultSet result) throws SQLException
    {
        Package _package = new Package();

        _package.setId(result.getLong("package_id"));
        _package.setDescription(result.getString("package_description"));
        _package.setUpc(result.getString("package_upc"));
        _package.setBrand(result.getString("package_brand"));
        _package.setManufacturer(result.getString("package_manufacturer"));
        _package.setSize(result.getDouble("package_size"));
        _package.setSizeUnitOfMeasure(
                result.getString("package_size_unit_of_measure"));
        _package.setStorageType(result.getString("storage_type"));
        _package.setStorageStatements(result.getString("storage_statements"));
        _package.setHealthClaims(result.getString("health_claims"));
        _package.setOtherPackageStatements(
                result.getString("other_package_statements"));
        _package.setSuggestedDirections(
                result.getString("suggested_directions"));
        _package.setIngredients(result.getString("ingredients"));
        _package.setMultiPartFlag(result.getBoolean("multi_part_flag"));
        _package.setNutritionFactTable(
                result.getString("nutrition_fact_table"));
        _package.setAsPreparedPerServingAmount(
                result.getDouble("as_prepared_per_serving_amount"));
        _package.setAsPreparedUnitOfMeasure(
                result.getString("as_prepared_unit_of_measure"));
        _package.setAsSoldPerServingAmount(
                result.getDouble("as_sold_per_serving_amount"));
        _package.setAsSoldUnitOfMeasure(
                result.getString("as_sold_unit_of_measure"));
        _package.setAsPreparedPerServingAmountInGrams(
                result.getDouble("as_prepared_per_serving_amount_in_grams"));
        _package.setAsSoldPerServingAmountInGrams(
                result.getDouble("as_sold_per_serving_amount_in_grams"));
        _package.setPackageComment(result.getString("package_comment"));
        _package.setPackageSource(result.getString("package_source"));
        _package.setPackageCollectionDate(
                result.getDate("package_collection_date"));
        _package.setNumberOfUnits(result.getInt("number_of_units"));
        _package.setCreationDate(result.getTimestamp("creation_date"));
        _package.setLastEditDate(result.getTimestamp("last_edit_date"));
        _package.setEditedBy(result.getString("edited_by"));
        _package.setProductId(result.getLong("package_product_id_fkey"));

        return _package;
    }

    public static PackageResponse getPackageResponse(ResultSet resultSet)
            throws SQLException
    {
        Package _package = getPackage(resultSet);
        PackageResponse packageResponse = new PackageResponse(_package);

        return packageResponse;
    }

    public static Map<String, Object> getQueryMap(PackageRequest request)
    {
        Map<String, Object> queryMap = new HashMap<String, Object>();

        if (!request.labelUpc.isEmpty())
            if (StringUtilities.isNumeric(request.labelUpc))
                queryMap.put("package_upc", request.labelUpc);
            else
                queryMap.put("inputError", ResponseCodes.INVALID_UPC);

        if (!request.labelDescription.isEmpty())
            queryMap.put("package_description", request.labelDescription);
        if (!request.labelSource.isEmpty())
            queryMap.put("package_source", request.labelSource);
        if (!request.labelIngredients.isEmpty())
            queryMap.put("ingredients", request.labelIngredients);

        if (DateUtil.validateDates(request.collectionDateFrom,
                request.collectionDateTo))
        {
            if (!request.collectionDateFrom.isEmpty()
                    && !request.collectionDateTo.isEmpty())
            {
                queryMap.put("collection_date_from",
                        request.collectionDateFrom);
                queryMap.put("collection_date_to", request.collectionDateTo);
            }
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_DATE);

        if (request.offset != null)
        {
            if (!isType(request.offset.toString(), "int"))
                queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);
        }
        else
            queryMap.put("inputError", ResponseCodes.INVALID_INTEGER);

        return queryMap;
    }

    public static Boolean isType(String testStr, String type)
    {
        try
        {
            if (type.equalsIgnoreCase("float"))
                Float.parseFloat(testStr);
            else if (type.equalsIgnoreCase("int"))
                Integer.parseInt(testStr);
            else if (type.equalsIgnoreCase("double"))
                Double.parseDouble(testStr);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

}
