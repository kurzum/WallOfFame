/*
 * This code is in the public domain and may be used in any way you see fit, with the following conditions:
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package org.dbpedia.walloffame.webtest.page.customer

import org.dbpedia.walloffame.webtest.page.Page
import org.openqa.selenium.WebDriver

class EditCustomerPage(driver: WebDriver) extends Page[EditCustomerPage]("customer-edit", driver) {
  def customerName = valueOf(input("name"))

  def updateCustomer(customerName: String) = {
    set(input("name"), customerName)
    submitExpecting[ViewCustomerPage]
  }

  def updateCustomerExpectingFailure(customerName: String) = {
    set(input("name"), customerName)
    submitExpecting[EditCustomerPage]
  }
}