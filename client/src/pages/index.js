import React from "react"

import Layout from "../components/layout"
import SEO from "../components/seo"
import AppTable from "../components/app-table";

const IndexPage = () => {

    return (
        <Layout>
            <SEO title="Home"/>
            <p>Here you can find all applications hosted on this site.</p>
            <AppTable/>
        </Layout>
    )
}

export default IndexPage
