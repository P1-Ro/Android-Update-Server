import React, {useEffect, useState} from "react"
import Loader from "../components/loader";

import {Container, Row, Col} from 'react-grid-system';

function getData() {
    return fetch(`/api/info`)
        .then(response => response.json()) // parse JSON from request
        .then(resultData => {
            return resultData.apps;
        })
}

function renderTable(tableData) {

    const rows = []
    for (let data of tableData) {
        rows.push(
            <Row key={data.packageName}>
                <Col sm={3}>
                    <img alt={`Icon of ${data.name}`} src={`data:image/jpeg;base64,${data.icon}`}/>
                </Col>
                <Col sm={3} className='valign'>
                    {data.name}
                </Col>
                <Col sm={3} className='valign'>
                    {data.versionName}
                </Col>
                <Col sm={3} className='valign'>
                    <a href={`/api/download/${data.packageName}`}>Download</a>
                </Col>
            </Row>
        )
    }

    return (
        <Container>
            <Row style={{
                fontWeight: "bold",
                borderBottom: "1px solid rebeccapurple",
                marginBottom: "20px",
                paddingBottom: "5px"
            }}>
                <Col sm={3}>
                    Icon
                </Col>
                <Col sm={3}>
                    Name
                </Col>
                <Col sm={3}>
                    Version
                </Col>
                <Col sm={3}/>
            </Row>
            {rows}
        </Container>
    )
}


const AppTable = () => {

    const [tableData, setTableData] = useState(undefined)
    useEffect(() => {
        getData().then(data => setTableData(data));
    }, [])


    if (tableData) {
        if (tableData.length) {
            return renderTable(tableData)
        } else {
            return (
                <p>
                    It's so empty here :( <br/>
                    Try uploading some apk
                </p>
            )
        }
    } else {
        return (
            <Loader/>
        )
    }

}

export default AppTable
