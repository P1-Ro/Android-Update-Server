import React from "react"

import Layout from "../components/layout"
import SEO from "../components/seo"

const NotFoundPage = () => (
  <Layout>
    <SEO title="500: Server error" />
    <h1>ERROR</h1>
    <p>Uh oh... something went horribly wrong.</p>
  </Layout>
)

export default NotFoundPage
